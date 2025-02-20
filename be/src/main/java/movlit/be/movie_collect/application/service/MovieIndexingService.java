package movlit.be.movie_collect.application.service;

import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import movlit.be.movie_collect.infra.jpa.MovieCollectRepository;
import movlit.be.movie.application.converter.main.MovieDocumentConverter;
import movlit.be.movie.domain.document.MovieDocument;
import movlit.be.movie.infra.persistence.es.MovieDocumentRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class MovieIndexingService {

    private final MovieCollectRepository movieRepository;
    private final MovieDocumentRepository movieDocumentRepository;

    private static final int BATCH_SIZE = 40;

    public void indexMovies() {
        var movies = movieRepository.findAll();
        List<MovieDocument> batch = new ArrayList<>();

        int count = 0;
        for (var movie : movies) {
            batch.add(MovieDocumentConverter.entityToDocument(movie));
            count++;
            if (count % BATCH_SIZE == 0) {
                movieDocumentRepository.saveAll(batch);
                batch.clear();
                // API 혹은 ES 부하 조절을 위한 잠시 대기
                sleepSilently(1000);
            }
        }
        if (!batch.isEmpty()) {
            movieDocumentRepository.saveAll(batch);
        }
        log.info("Indexed {} movies", count);
    }

    private void sleepSilently(long millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            log.warn("Sleep interrupted", e);
        }
    }

}
