package movlit.be.movie_collection.application.service;

import jakarta.transaction.Transactional;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import movlit.be.common.util.IdFactory;
import movlit.be.common.util.ids.MovieCrewId;
import movlit.be.movie.application.converter.detail.MovieConvertor;
import movlit.be.movie.domain.MovieRole;
import movlit.be.movie.domain.ProductionCountry;
import movlit.be.movie.domain.entity.MovieCrewEntity;
import movlit.be.movie.domain.entity.MovieEntity;
import movlit.be.movie.domain.entity.MovieGenreEntity;
import movlit.be.movie.domain.entity.MovieGenreIdForEntity;
import movlit.be.movie.domain.entity.MovieRCrewEntity;
import movlit.be.movie.domain.entity.MovieRCrewIdForEntity;
import movlit.be.movie.domain.entity.MovieTagEntity;
import movlit.be.movie.domain.entity.MovieTagIdForEntity;
import movlit.be.movie.infra.persistence.jpa.MovieCrewJpaRepository;
import movlit.be.movie.infra.persistence.jpa.MovieRCrewJpaRepository;
import movlit.be.movie_collection.application.TmdbApiClient;
import movlit.be.movie_collection.infra.jpa.MovieCollectRepository;
import movlit.be.movie_collection.infra.jpa.MovieGenreCollectRepository;
import movlit.be.movie_collection.infra.jpa.MovieTagRepository;
import movlit.be.movie_heart_count.application.service.MovieHeartCountService;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class MovieCollectionService {

    private final TmdbApiClient tmdbApiClient;
    private final MovieCollectRepository movieCollectRepository;
    private final MovieTagRepository movieTagRepository;
    private final MovieGenreCollectRepository movieGenreCollectRepository;
    private final MovieCrewJpaRepository movieCrewJpaRepository;
    private final MovieRCrewJpaRepository movieRCrewJpaRepository;
    private final MovieHeartCountService movieHeartCountService;

    // 상수 정의
    private static final int MAX_DISCOVER_PAGE = 5;
    private static final int DISCOVER_SLEEP_MOD = 2;
    private static final int KEYWORD_GENRE_SLEEP_MOD = 40;
    private static final int SLEEP_INTERVAL_MILLIS = 1000;

    public void collectDiscoverMovies() {
        for (int i = 1; i <= MAX_DISCOVER_PAGE; i++) {
            List<Map<String, Object>> discoverResults = tmdbApiClient.fetchDiscoverMovies(String.valueOf(i));
            if (discoverResults.isEmpty()) {
                break;
            }
            List<MovieEntity> movieEntities = new ArrayList<>();
            for (Map<String, Object> result : discoverResults) {
                String apiId = String.valueOf(result.get("id"));
                Map<String, Object> detailResult = tmdbApiClient.fetchMovieDetails(apiId);
                MovieEntity movie = convertToMovieEntity(result, detailResult);
                if (movie != null) {
                    movieEntities.add(movie);
                    log.info("Processed movie id={}", movie.getMovieId());
                }
            }
            movieCollectRepository.saveAll(movieEntities);
            if (i % DISCOVER_SLEEP_MOD == 0) {
                sleep(SLEEP_INTERVAL_MILLIS);
            }
        }
    }

    private MovieEntity convertToMovieEntity(Map<String, Object> result, Map<String, Object> detailResult) {
        LocalDate today = LocalDate.now();
        Integer id = (Integer) result.get("id");
        String title = (String) result.get("title");
        String originalTitle = (String) result.get("original_title");
        String overview = (String) result.get("overview");
        Double popularity = (Double) result.get("popularity");

        String posterPath = Optional.ofNullable((String) result.get("poster_path")).orElse("");
        if (!posterPath.isEmpty()) {
            posterPath = "http://image.tmdb.org/t/p/original" + posterPath;
        }

        String backdropPath = Optional.ofNullable((String) result.get("backdrop_path")).orElse("");
        if (!backdropPath.isEmpty()) {
            backdropPath = "http://image.tmdb.org/t/p/original" + backdropPath;
        }

        String releaseDateStr = (String) result.get("release_date");
        LocalDate releaseDate = LocalDate.parse(releaseDateStr, DateTimeFormatter.ISO_LOCAL_DATE);
        if (releaseDate.isAfter(today)) {
            return null; // 미래 개봉 영화는 스킵
        }

        String originalLanguage = (String) result.get("original_language");
        Long voteCount = Long.valueOf((Integer) result.get("vote_count"));
        Double voteAverage = (Double) result.get("vote_average");

        String productionCountry = "NONE";
        List<Map<String, Object>> productionCountries = (List<Map<String, Object>>) detailResult.get(
                "production_countries");
        if (productionCountries != null && !productionCountries.isEmpty()) {
            productionCountry = ProductionCountry.getNameFromCode(
                    (String) productionCountries.get(0).get("iso_3166_1"));
        }
        Integer runtime = (Integer) detailResult.get("runtime");
        String status = (String) detailResult.get("status");
        String tagline = (String) detailResult.get("tagline");

        MovieEntity movie = MovieEntity.builder()
                .movieId(Long.valueOf(id))
                .title(title)
                .originalTitle(originalTitle)
                .overview(overview)
                .popularity(popularity)
                .posterPath(posterPath)
                .backdropPath(backdropPath)
                .releaseDate(releaseDate)
                .originalLanguage(originalLanguage)
                .voteCount(voteCount)
                .voteAverage(voteAverage)
                .productionCountry(productionCountry)
                .runtime(runtime)
                .status(status)
                .tagline(tagline)
                .regDt(LocalDateTime.now())
                .updDt(LocalDateTime.now())
                .delYn(false)
                .build();

        // 하트 카운트 저장 처리
        movieHeartCountService.save(MovieConvertor.toMovieHeartCountEntity(movie.getMovieId()));
        return movie;
    }

    public void collectMovieKeywords() {
        List<MovieEntity> movies = movieCollectRepository.findAll();
        int count = 0;
        for (MovieEntity movie : movies) {
            fetchAndSaveMovieKeywords(movie);
            count++;
            if (count % KEYWORD_GENRE_SLEEP_MOD == 0) {
                sleep(SLEEP_INTERVAL_MILLIS);
            }
            log.info("Processed keywords for movie id={}", movie.getMovieId());
        }
    }

    private List<MovieTagEntity> fetchAndSaveMovieKeywords(MovieEntity movie) {
        Map<String, Object> keywordResponse = tmdbApiClient.fetchMovieKeywords(movie.getMovieId());
        List<MovieTagEntity> tagEntities = new ArrayList<>();
        if (keywordResponse == null || keywordResponse.get("keywords") == null) {
            return List.of();
        }
        List<Map<String, Object>> keywords = (List<Map<String, Object>>) keywordResponse.get("keywords");
        for (Map<String, Object> keyword : keywords) {
            Long id = Long.valueOf((Integer) keyword.get("id"));
            String name = (String) keyword.get("name");
            MovieTagIdForEntity tagId = new MovieTagIdForEntity(id, movie.getMovieId());
            MovieTagEntity tag = MovieTagEntity.builder()
                    .movieTagIdForEntity(tagId)
                    .name(name)
                    .movieEntity(movie)
                    .regDt(LocalDateTime.now())
                    .updDt(LocalDateTime.now())
                    .delYn(false)
                    .build();
            tagEntities.add(tag);
        }
        movieTagRepository.saveAll(tagEntities);
        return tagEntities;
    }

    public void collectMovieGenres() {
        List<MovieEntity> movies = movieCollectRepository.findAll();
        int count = 0;
        for (MovieEntity movie : movies) {
            fetchAndSaveMovieGenres(movie);
            count++;
            if (count % KEYWORD_GENRE_SLEEP_MOD == 0) {
                sleep(SLEEP_INTERVAL_MILLIS);
            }
            log.info("Processed genres for movie id={}", movie.getMovieId());
        }
    }

    private List<MovieGenreEntity> fetchAndSaveMovieGenres(MovieEntity movie) {
        Map<String, Object> detailResponse = tmdbApiClient.fetchMovieDetails(movie.getMovieId().toString());
        List<MovieGenreEntity> genreEntities = new ArrayList<>();
        List<Map<String, Object>> genres = (List<Map<String, Object>>) detailResponse.get("genres");
        if (genres != null) {
            Set<MovieGenreIdForEntity> genreIdSet = new LinkedHashSet<>();
            genres.forEach(genre -> {
                Integer apiGenreId = (Integer) genre.get("id");
                Long genreId = mapApiGenreIdToServiceGenreId(apiGenreId);
                genreIdSet.add(new MovieGenreIdForEntity(movie.getMovieId(), genreId));
            });
            for (MovieGenreIdForEntity id : genreIdSet) {
                MovieGenreEntity genreEntity = new MovieGenreEntity(id, movie);
                genreEntities.add(genreEntity);
            }
            movieGenreCollectRepository.saveAll(genreEntities);
        }
        return genreEntities;
    }

    private Long mapApiGenreIdToServiceGenreId(int apiGenreId) {
        return switch (apiGenreId) {
            case 28, 12 -> 1L;
            case 16 -> 2L;
            case 35 -> 3L;
            case 80 -> 4L;
            case 99 -> 5L;
            case 18, 10751 -> 6L;
            case 14 -> 7L;
            case 36 -> 8L;
            case 10402 -> 9L;
            case 9648 -> 10L;
            case 10749 -> 11L;
            case 878 -> 12L;
            case 10770 -> 13L;
            case 27, 53 -> 14L;
            case 10752 -> 15L;
            case 37 -> 16L;
            default -> 99999L;
        };
    }

    public void collectMovieCrew() {
        List<MovieEntity> movies = movieCollectRepository.findAll();
        List<MovieCrewEntity> crewEntities = new ArrayList<>();
        List<MovieRCrewEntity> movieRCrewEntities = new ArrayList<>();

        for (MovieEntity movie : movies) {
            Map<String, Object> creditsResponse = tmdbApiClient.fetchMovieCredits(movie.getMovieId());
            // 캐스트 처리
            List<Map<String, Object>> castList = (List<Map<String, Object>>) creditsResponse.get("cast");
            if (castList != null) {
                for (Map<String, Object> cast : castList) {
                    MovieCrewEntity crewEntity = createMovieCrewEntityFromCast(cast);
                    MovieRCrewEntity rCrewEntity = createMovieRCrewEntity(movie, crewEntity);
                    crewEntities.add(crewEntity);
                    movieRCrewEntities.add(rCrewEntity);
                    log.info("Processed cast member: {}", crewEntity.getName());
                }
            }
            // 크루에서 감독(Director) 처리
            List<Map<String, Object>> crewList = (List<Map<String, Object>>) creditsResponse.get("crew");
            if (crewList != null && !crewList.isEmpty()) {
                Optional<Map<String, Object>> directorOpt = crewList.stream()
                        .filter(crew -> "Director".equals(crew.get("job")))
                        .findFirst();
                if (directorOpt.isPresent()) {
                    Map<String, Object> directorMap = directorOpt.get();
                    MovieCrewEntity directorEntity = createMovieCrewEntityForDirector(directorMap);
                    MovieRCrewEntity rCrewEntity = createMovieRCrewEntity(movie, directorEntity);
                    crewEntities.add(directorEntity);
                    movieRCrewEntities.add(rCrewEntity);
                    log.info("Processed director: {}", directorEntity.getName());
                }
            }
        }
        movieCrewJpaRepository.saveAll(crewEntities);
        movieRCrewJpaRepository.saveAll(movieRCrewEntities);
    }

    private MovieCrewEntity createMovieCrewEntityFromCast(Map<String, Object> cast) {
        MovieCrewId crewId = IdFactory.createMovieCrewId();
        String name = (String) cast.get("name");
        MovieRole role = MovieRole.CAST;
        String charName = (String) cast.get("character");
        String profileImgUrl = (String) cast.get("profile_path");
        int orderNo = (Integer) cast.get("order");
        return MovieCrewEntity.builder()
                .movieCrewId(crewId)
                .name(name)
                .role(role)
                .charName(charName)
                .profileImgUrl(profileImgUrl)
                .orderNo(orderNo)
                .build();
    }

    private MovieCrewEntity createMovieCrewEntityForDirector(Map<String, Object> crew) {
        MovieCrewId crewId = IdFactory.createMovieCrewId();
        String name = (String) crew.get("name");
        MovieRole role = MovieRole.DIRECTOR;
        String charName = (String) crew.get("character");
        String profileImgUrl = (String) crew.get("profile_path");
        int orderNo = -1;
        return MovieCrewEntity.builder()
                .movieCrewId(crewId)
                .name(name)
                .role(role)
                .charName(charName)
                .profileImgUrl(profileImgUrl)
                .orderNo(orderNo)
                .build();
    }

    private MovieRCrewEntity createMovieRCrewEntity(MovieEntity movie, MovieCrewEntity crewEntity) {
        MovieRCrewIdForEntity rCrewId = new MovieRCrewIdForEntity(movie.getMovieId(), crewEntity.getMovieCrewId());
        return new MovieRCrewEntity(rCrewId, crewEntity, movie);
    }

    private void sleep(int millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new RuntimeException(e);
        }
    }

}
