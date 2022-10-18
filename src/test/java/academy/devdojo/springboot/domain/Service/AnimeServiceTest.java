package academy.devdojo.springboot.domain.Service;

import academy.devdojo.springboot.domain.Anime;
import academy.devdojo.springboot.domain.Exception.ResourcesNotFoundException;
import academy.devdojo.springboot.domain.Repository.AnimeRepository;
import academy.devdojo.springboot.domain.Util.Utils;
import academy.devdojo.springboot.domain.animeCreator.AnimeCreator;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import java.util.List;
import java.util.Optional;

@ExtendWith(SpringExtension.class)
class AnimeServiceTest {
    @InjectMocks //directly testing the class
    AnimeService animeService;
    @Mock //testing the behaviour of related classes whom animeService depend on {Anime Repository + Utils}
    private AnimeRepository animeRepository;
    @Mock
    private Utils utils;
    @BeforeEach//{Mock Anime Repository + Utils Attributes}
    public void setUp(){
        //anime page
        PageImpl<Anime> animePage = new PageImpl<>(List.of(AnimeCreator.createValidAnime()));
        //list all anime + find by ID
        BDDMockito.when(animeRepository.findAll(ArgumentMatchers.any(PageRequest.class))).thenReturn(animePage);
        BDDMockito.when(animeRepository.findById(ArgumentMatchers.anyInt())).thenReturn(Optional.ofNullable(AnimeCreator.createValidAnime()));
        //find by name
        BDDMockito.when(animeRepository.findByName(ArgumentMatchers.anyString())).thenReturn(List.of(AnimeCreator.createValidAnime()));
        //saved anime
        BDDMockito.when(animeRepository.save(AnimeCreator.createAnimeToBeSaved())).thenReturn(AnimeCreator.createValidAnime());
        //delete anime
        BDDMockito.doNothing().when(animeRepository).delete(ArgumentMatchers.any(Anime.class));
        //update anime info
        BDDMockito.when(animeRepository.save(AnimeCreator.createValidAnime())).thenReturn(AnimeCreator.updatedAnimeInfo());
        //Utils find anime or throw exception
        BDDMockito.when(utils.findAnimeOrThrowExp(ArgumentMatchers.anyInt(),ArgumentMatchers.any(AnimeRepository.class))).thenReturn(AnimeCreator.createValidAnime());
        //AnimeService Delete Method Throws Custom Resources Not Found Exception When Anime Doesn't Exist

    }
    @Test
    @DisplayName("ListAll Return A Pageable List of Animes When Successful")
    public void listAll_ReturnsListOfAnimesInsidePageObject_WhenSuccessful(){
        String expectedName = AnimeCreator.createValidAnime().getName();
        Page<Anime> animePage = animeService.listAll(PageRequest.of(1,2));
        Assertions.assertThat(animePage).isNotNull();
        Assertions.assertThat(animePage.toList()).isNotNull();
        Assertions.assertThat(animePage.toList().get(0).getName()).isEqualTo(expectedName);
    }
    @Test
    @DisplayName("Find By Id Returns Anime When Animes Is Successful")
    public void findById_ReturnsListOfAnimesInsidePageObject_WhenSuccessful(){
        Integer expectedId = AnimeCreator.createValidAnime().getId();
        Anime animeId = animeService.findAnimeUtilById(100);
        Assertions.assertThat(animeId).isNotNull();
        Assertions.assertThat(animeId.getId()).isNotNull();
        Assertions.assertThat(animeId.getId()).isEqualTo(expectedId);
    }
    @Test
    @DisplayName("Find By Name Returns Pageable Anime When Pageable Anime Is Successful")
    public void findByName_ReturnsListOfAnimesInsidePageObject_WhenSuccessful(){
        String animeName = AnimeCreator.createValidAnime().getName();
        List<Anime> animeList = animeService.findByName("Monster");
        Assertions.assertThat(animeList).isNotNull();
        Assertions.assertThat(animeList).isNotEmpty();
        Assertions.assertThat(animeList.get(0).getName()).isEqualTo(animeName);
    }
    @Test
    @DisplayName("Save Creates An Anime When Successful")
    public void save_CreatesAnAnime_WhenSuccessful(){
        Integer expectedId = AnimeCreator.createValidAnime().getId();
        Anime animeToBeSaved = AnimeCreator.createAnimeToBeSaved();
        Anime anime = animeService.save(AnimeCreator.createAnimeToBeSaved());
        Assertions.assertThat(anime).isNotNull();
        Assertions.assertThat(anime.getId()).isNotNull();
        Assertions.assertThat(anime.getId()).isEqualTo(expectedId);
        Assertions.assertThat(anime.getName()).isEqualTo(animeToBeSaved.getName());
    }
    @Test
    @DisplayName("Delete Created Anime When Successful")
    public void Delete_CreatedAnime_WhenSuccessful(){
        Assertions.assertThatCode(() -> animeService.delete(100)).doesNotThrowAnyException();
    }
    @Test
    @DisplayName("Delete Throws ResourceNotFoundException When Anime Does Not Exist")
    public void DeleteThrows_ResourceNotFoundException_WhenAnimeUnExisted(){
        BDDMockito.when(utils.findAnimeOrThrowExp(ArgumentMatchers.anyInt(),ArgumentMatchers.any(AnimeRepository.class))).thenThrow(new ResourcesNotFoundException("Anime Not Found"));

        Assertions.assertThatExceptionOfType(ResourcesNotFoundException.class).isThrownBy(()->animeService.delete(100));

    }
    @Test
    @DisplayName("Updated Anime Info Anime When Successful")
    public void updatedAnimeInfo_WhenSuccessful(){
        Anime updatedAnimeInfo = AnimeCreator.updatedAnimeInfo();
        String expectedName = updatedAnimeInfo.getName();
        Anime animeBody = animeService.save(AnimeCreator.createValidAnime());
        Assertions.assertThat(animeBody).isNotNull();
        Assertions.assertThat(animeBody.getId()).isNotNull();
        Assertions.assertThat(animeBody.getName()).isEqualTo(expectedName);
    }



}