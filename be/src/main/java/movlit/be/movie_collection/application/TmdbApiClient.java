package movlit.be.movie_collection.application;

import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.HttpHeaders;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class TmdbApiClient {

    private final RestTemplate restTemplate;
    private final String apiKey;

    // API 호출에 사용할 상수
    private static final String LANGUAGE_KO = "&language=ko";
    private static final String REGION_KR = "&region=KR";
    private static final String INCLUDE_ADULT_FALSE = "&include_adult=false";
    private static final String RELEASE_DATE_GTE = "&release_date.gte=2023-01-01";
    private static final String RELEASE_DATE_LTE = "&release_date.lte=2024-12-31";
    private static final String SORT_BY = "&sort_by=vote_average";

    public TmdbApiClient(RestTemplateBuilder builder,
                         @Value("${tmdb.key}") String apiKey,
                         @Value("${tmdb.accessToken}") String accessToken) {
        this.apiKey = apiKey;
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json");
        headers.add("Authorization", "Bearer " + accessToken);
        this.restTemplate = builder.build();
        this.restTemplate.getMessageConverters().add(0, new StringHttpMessageConverter(StandardCharsets.UTF_8));
    }

    public List<Map<String, Object>> fetchDiscoverMovies(String page) {
        String url = "https://api.themoviedb.org/3/discover/movie?api_key=" + apiKey +
                "&page=" + page + LANGUAGE_KO + REGION_KR + INCLUDE_ADULT_FALSE +
                RELEASE_DATE_GTE + RELEASE_DATE_LTE + SORT_BY;
        Map<String, Object> response = restTemplate.getForObject(url, Map.class);
        if (response == null || response.get("results") == null) {
            return List.of();
        }
        return (List<Map<String, Object>>) response.get("results");
    }

    public Map<String, Object> fetchMovieDetails(String apiId) {
        String url = "https://api.themoviedb.org/3/movie/" + apiId + "?api_key=" + apiKey + "&language=ko-KR";
        return restTemplate.getForObject(url, Map.class);
    }

    public Map<String, Object> fetchMovieKeywords(Long movieId) {
        String url = "https://api.themoviedb.org/3/movie/" + movieId + "/keywords?api_key=" + apiKey;
        return restTemplate.getForObject(url, Map.class);
    }

    public Map<String, Object> fetchMovieCredits(Long movieId) {
        String url = "https://api.themoviedb.org/3/movie/" + movieId + "/credits?api_key=" + apiKey + LANGUAGE_KO;
        return restTemplate.getForObject(url, Map.class);
    }

}
