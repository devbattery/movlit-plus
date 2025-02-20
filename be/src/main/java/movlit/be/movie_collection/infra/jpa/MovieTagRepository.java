package movlit.be.movie_collection.infra.jpa;

import movlit.be.movie.domain.entity.MovieTagEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MovieTagRepository extends JpaRepository<MovieTagEntity, Long> {

}
