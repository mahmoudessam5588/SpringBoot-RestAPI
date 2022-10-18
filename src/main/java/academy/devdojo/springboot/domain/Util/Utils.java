package academy.devdojo.springboot.domain.Util;

import academy.devdojo.springboot.domain.Anime;
import academy.devdojo.springboot.domain.Exception.ResourcesNotFoundException;
import academy.devdojo.springboot.domain.Repository.AnimeRepository;
import org.springframework.stereotype.Component;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Component
public class Utils {
    public String FormattingLocalDate(LocalDateTime localDateTime){
        return DateTimeFormatter.ofPattern("yyy-MMM-dd HH:mm:ss").format(localDateTime);
    }
    public Anime findAnimeOrThrowExp(Integer id , AnimeRepository animeRepository){
        return animeRepository
                .findById(id)
                .orElseThrow(() -> new ResourcesNotFoundException("anime not found"));
    }
}
