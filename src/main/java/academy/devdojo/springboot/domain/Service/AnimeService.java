package academy.devdojo.springboot.domain.Service;

import academy.devdojo.springboot.domain.Anime;
import academy.devdojo.springboot.domain.Repository.AnimeRepository;
import academy.devdojo.springboot.domain.Util.Utils;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class AnimeService {
    private  final Utils utils;
    private final AnimeRepository animeRepository;

    public Page<Anime> listAll(Pageable pageable) {
        return animeRepository.findAll(pageable);
    }
    //for roll back purposes
    //In-case of exception (rollBackForClassName = Exception.class)
    @Transactional
    public List<Anime> findByName(String name){return animeRepository.findByName(name);}
    public Anime findAnimeUtilById(Integer id){
        return utils.findAnimeOrThrowExp(id,animeRepository);
    }
    public Anime save(Anime anime) {return animeRepository.save(anime);}
    public void delete(Integer id){
        animeRepository.delete(utils.findAnimeOrThrowExp(id ,animeRepository));
    }
    public void replace(Anime anime){
       animeRepository.save(anime);
    }


    }

