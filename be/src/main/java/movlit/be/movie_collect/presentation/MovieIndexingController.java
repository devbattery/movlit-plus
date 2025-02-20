package movlit.be.movie_collect.presentation;

import lombok.RequiredArgsConstructor;
import movlit.be.movie_collect.application.service.MovieIndexingService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
