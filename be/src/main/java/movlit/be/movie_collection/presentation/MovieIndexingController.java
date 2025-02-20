package movlit.be.movie_collection.presentation;

import lombok.RequiredArgsConstructor;
import movlit.be.movie_collection.application.service.MovieIndexingService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/collect/indices")
@RequiredArgsConstructor
public class MovieIndexingController {

    private final MovieIndexingService movieIndexingService;

    @GetMapping("/movies")
    public ResponseEntity<?> indexMovies() {
        movieIndexingService.indexMovies();
        return ResponseEntity.ok().build();
    }

}
