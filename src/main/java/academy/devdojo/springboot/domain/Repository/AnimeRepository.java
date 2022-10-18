package academy.devdojo.springboot.domain.Repository;

import academy.devdojo.springboot.domain.Anime;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface AnimeRepository extends JpaRepository<Anime,Integer> {
    List<Anime>findByName(String name);
}
