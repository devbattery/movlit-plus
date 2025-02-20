package movlit.be.data_collection.movie;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import movlit.be.movie_collection.application.service.MovieCollectionService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/collect/movie")
@RequiredArgsConstructor
@Slf4j
public class MovieCollectionController {

    private final MovieCollectionService movieCollectionService;

    @GetMapping("/discover")
    public ResponseEntity<Void> collectDiscoverMovies() {
        movieCollectionService.collectDiscoverMovies();
        return ResponseEntity.ok().build();
    }

    @GetMapping("/keywords")
    public ResponseEntity<Void> collectMovieKeywords() {
        movieCollectionService.collectMovieKeywords();
        return ResponseEntity.ok().build();
    }

    @GetMapping("/genres")
    public ResponseEntity<Void> collectMovieGenres() {
        movieCollectionService.collectMovieGenres();
        return ResponseEntity.ok().build();
    }

    @GetMapping("/discover/crew")
    public ResponseEntity<Void> collectMovieCrew() {
        movieCollectionService.collectMovieCrew();
        return ResponseEntity.ok().build();
    }

}
