package movlit.be.movie.infra.persistence;

import co.elastic.clients.elasticsearch._types.query_dsl.BoolQuery;
import co.elastic.clients.elasticsearch._types.query_dsl.FunctionBoostMode;
import co.elastic.clients.elasticsearch._types.query_dsl.FunctionScore;
import co.elastic.clients.elasticsearch._types.query_dsl.FunctionScoreMode;
import co.elastic.clients.elasticsearch._types.query_dsl.FunctionScoreQuery;
import co.elastic.clients.elasticsearch._types.query_dsl.NestedQuery;
import co.elastic.clients.elasticsearch._types.query_dsl.Query;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import movlit.be.common.util.Genre;
import movlit.be.movie.application.converter.main.MovieDocumentConverter;
import movlit.be.movie.domain.Movie;
import movlit.be.movie.domain.document.MovieDocument;
import movlit.be.movie.domain.repository.MovieSearchRepository;
import movlit.be.movie.presentation.dto.response.MovieCrewResponseDto;
import movlit.be.movie.presentation.dto.response.MovieDocumentResponseDto;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.elasticsearch.client.elc.NativeQuery;
import org.springframework.data.elasticsearch.client.elc.NativeQueryBuilder;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.SearchHit;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
@Slf4j
public class MovieSearchRepositoryImpl implements MovieSearchRepository {

    private final ElasticsearchOperations elasticsearchOperations;

    @Override
    public List<Movie> searchMovieByMemberInterestGenre(List<Genre> genreList, Pageable pageable) {
        Query query = buildMemberInterestGenreQuery(genreList);
        SearchHits<MovieDocument> searchHits = executeSearch(query, pageable, MovieDocument.class);
        return convertToMovies(searchHits);
    }

    private Query buildMemberInterestGenreQuery(List<Genre> genreList) {
        // 별도 메서드로 분리한 장르 nested 쿼리와 각 장르별 가중치 함수 사용
        Query genreNestedQuery = buildGenreNestedQuery(genreList);
        List<FunctionScore> functions = genreList.stream()
                .map(this::buildGenreFunctionScore)
                .collect(Collectors.toList());

        return FunctionScoreQuery.of(f -> f
                .query(genreNestedQuery)
                .functions(functions)
                .scoreMode(FunctionScoreMode.Sum)
                .boostMode(FunctionBoostMode.Sum)
        )._toQuery();
    }

    private Query buildGenreNestedQuery(List<Genre> genreList) {
        List<Query> genreQueries = genreList.stream()
                .map(genre -> Query.of(q -> q.term(t -> t.field("movieGenre.genreId").value(genre.getId()))))
                .collect(Collectors.toList());

        return NestedQuery.of(n -> n
                .path("movieGenre")
                .query(q -> q.bool(b -> b.should(genreQueries)))
        )._toQuery();
    }

    private FunctionScore buildGenreFunctionScore(Genre genre) {
        Query filterQuery = NestedQuery.of(n -> n
                .path("movieGenre")
                .query(q -> q.term(t -> t.field("movieGenre.genreId").value(genre.getId())))
        )._toQuery();

        return FunctionScore.of(f -> f.filter(filterQuery).weight(1.5));
    }

    @Override
    public List<Movie> searchMovieByMemberHeartCrew(List<MovieCrewResponseDto> crewList, Pageable pageable) {
        Set<String> crewNameSet = extractCrewNames(crewList);
        log.info("CrewNameSet: {}", crewNameSet);

        // mustNot 쿼리: 제외할 movieId 목록 생성
        List<Query> mustNotQueries = buildCrewMustNotQueries(crewList);

        // 제작진 이름에 대한 nested 쿼리
        Query crewNestedQuery = buildCrewNestedQuery(crewNameSet);

        BoolQuery topLevelBoolQuery = BoolQuery.of(b -> b
                .must(crewNestedQuery)
                .mustNot(mustNotQueries)
        );

        // 제작진 이름에 대해 가중치 함수 생성
        List<FunctionScore> functions = buildCrewFunctionScores(crewNameSet);

        Query query = FunctionScoreQuery.of(f -> f
                .query(topLevelBoolQuery._toQuery())
                .functions(functions)
                .scoreMode(FunctionScoreMode.Sum)
                .boostMode(FunctionBoostMode.Sum)
        )._toQuery();

        SearchHits<MovieDocument> searchHits = executeSearch(query, pageable, MovieDocument.class);
        log.info("Explain output: {}", searchHits.getSearchHits());
        return convertToMovies(searchHits);
    }

    private List<Query> buildCrewMustNotQueries(List<MovieCrewResponseDto> crewList) {
        return crewList.stream()
                .map(c -> Query.of(q -> q.term(t -> t.field("movieId").value(c.movieId()))))
                .collect(Collectors.toList());
    }

    private Query buildCrewNestedQuery(Set<String> crewNameSet) {
        List<Query> crewNameQueries = crewNameSet.stream()
                .flatMap(name -> Stream.of(
                        Query.of(q -> q.match(t -> t.field("movieCrew.name.ko").query(name))),
                        Query.of(q -> q.match(t -> t.field("movieCrew.name.en").query(name)))
                ))
                .collect(Collectors.toList());

        return NestedQuery.of(n -> n
                .path("movieCrew")
                .query(q -> q.bool(b -> b.should(crewNameQueries)))
        )._toQuery();
    }

    private Set<String> extractCrewNames(List<MovieCrewResponseDto> crewList) {
        Map<Long, Set<String>> groupedMap = crewList.stream()
                .filter(dto -> "C".equals(dto.role().getValue()))
                .collect(Collectors.groupingBy(
                        MovieCrewResponseDto::movieId,
                        Collectors.collectingAndThen(Collectors.toList(), list -> list.stream()
                                .sorted(Comparator.comparingInt(MovieCrewResponseDto::orderNo))
                                .limit(3)
                                .map(MovieCrewResponseDto::name)
                                .collect(Collectors.toSet())
                        )
                ));
        return groupedMap.values().stream().flatMap(Set::stream).collect(Collectors.toSet());
    }

    private List<FunctionScore> buildCrewFunctionScores(Set<String> crewNameSet) {
        List<FunctionScore> functions = new ArrayList<>();
        crewNameSet.forEach(name -> {
            functions.add(FunctionScore.of(f -> f
                    .filter(
                            NestedQuery.of(n -> n
                                    .path("movieCrew")
                                    .query(q -> q.match(t -> t.field("movieCrew.name.ko").query(name)))
                            )._toQuery()
                    )
                    .weight(1.5)
            ));
            functions.add(FunctionScore.of(f -> f
                    .filter(
                            NestedQuery.of(n -> n
                                    .path("movieCrew")
                                    .query(q -> q.match(t -> t.field("movieCrew.name.en").query(name)))
                            )._toQuery()
                    )
                    .weight(1.5)
            ));
        });
        return functions;
    }

    @Override
    public MovieDocumentResponseDto searchMovieList(String inputStr, Pageable pageable) {
        log.info("검색 시작 : {}", inputStr);
        Query query = buildSearchMovieListQuery(inputStr);
        SearchHits<MovieDocument> searchHits = executeSearch(query, pageable, MovieDocument.class);

        List<MovieDocument> result = searchHits.stream()
                .map(SearchHit::getContent)
                .toList();

        int pageSize = pageable.getPageSize();
        long totalHits = searchHits.getTotalHits();
        long totalPages = (totalHits + pageSize - 1) / pageSize;
        return new MovieDocumentResponseDto(result, totalPages);
    }

    private Query buildSearchMovieListQuery(String inputStr) {
        List<Query> queries = new ArrayList<>();
        queries.addAll(buildTitleQueries(inputStr));
        queries.addAll(buildGenreQueries(inputStr));
        queries.addAll(buildCrewQueries(inputStr));

        return Query.of(q -> q.bool(b -> b.should(queries)));
    }

    private List<Query> buildTitleQueries(String inputStr) {
        List<Query> titleQueries = new ArrayList<>();
        titleQueries.add(Query.of(q -> q.match(m -> m.field("title").query(inputStr).boost(1.8f))));
        titleQueries.add(Query.of(q -> q.match(m -> m.field("title.en").query(inputStr).boost(1.8f))));
        titleQueries.add(Query.of(q -> q.match(m -> m.field("title.ngram").query(inputStr).boost(1.8f))));
        titleQueries.add(
                Query.of(q -> q.fuzzy(f -> f.field("title.standard").value(inputStr).fuzziness("AUTO").boost(1.4f))));
        return titleQueries;
    }

    private List<Query> buildGenreQueries(String inputStr) {
        List<Query> genreQueries = new ArrayList<>();
        genreQueries.add(Query.of(q -> q.nested(n -> n
                .path("movieGenre")
                .query(Query.of(nq -> nq.term(t -> t.field("movieGenre.genreName").value(inputStr).boost(1.8f))))
        )));
        genreQueries.add(Query.of(q -> q.nested(n -> n
                .path("movieGenre")
                .query(Query.of(nq -> nq.match(m -> m.field("movieGenre.genreName.ko").query(inputStr).boost(1.8f))))
        )));
        genreQueries.add(Query.of(q -> q.nested(n -> n
                .path("movieGenre")
                .query(Query.of(nq -> nq.fuzzy(
                        f -> f.field("movieGenre.genreName.standard").value(inputStr).fuzziness("AUTO").boost(1.4f))))
        )));
        return genreQueries;
    }

    private List<Query> buildCrewQueries(String inputStr) {
        List<Query> crewQueries = new ArrayList<>();
        crewQueries.add(Query.of(q -> q.nested(n -> n
                .path("movieCrew")
                .query(Query.of(nq -> nq.match(m -> m.field("movieCrew.name.ko").query(inputStr).boost(1.8f))))
        )));
        crewQueries.add(Query.of(q -> q.nested(n -> n
                .path("movieCrew")
                .query(Query.of(nq -> nq.match(m -> m.field("movieCrew.name.en").query(inputStr).boost(1.8f))))
        )));
        crewQueries.add(Query.of(q -> q.nested(n -> n
                .path("movieCrew")
                .query(Query.of(nq -> nq.match(m -> m.field("movieCrew.name.ngram").query(inputStr).boost(1.8f))))
        )));
        crewQueries.add(Query.of(q -> q.nested(n -> n
                .path("movieCrew")
                .query(Query.of(nq -> nq.fuzzy(
                        f -> f.field("movieCrew.name.ngram").value(inputStr).fuzziness("AUTO").boost(1.4f))))
        )));
        return crewQueries;
    }

    private NativeQuery buildNativeQuery(Query query, Pageable pageable) {
        NativeQuery nativeQuery = new NativeQueryBuilder()
                .withQuery(query)
                .withPageable(pageable)
                .withSort(Sort.by(Sort.Order.desc("_score")))
                .build();
        nativeQuery.setExplain(true);
        return nativeQuery;
    }

    private <T> SearchHits<T> executeSearch(Query query, Pageable pageable, Class<T> clazz) {
        NativeQuery nativeQuery = buildNativeQuery(query, pageable);
        return elasticsearchOperations.search(nativeQuery, clazz);
    }

    private List<Movie> convertToMovies(SearchHits<MovieDocument> searchHits) {
        if (!searchHits.hasSearchHits()) {
            return new ArrayList<>();
        }
        return searchHits.stream()
                .map(hit -> MovieDocumentConverter.documentToDomain(hit.getContent()))
                .toList();
    }

}
