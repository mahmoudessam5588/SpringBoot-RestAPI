package academy.devdojo.springboot.domain.Controller;

import academy.devdojo.springboot.domain.Anime;
import academy.devdojo.springboot.domain.Service.AnimeService;
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
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import java.util.List;

@ExtendWith(SpringExtension.class)
class AnimeControllerTest {
    @InjectMocks //directly testing the class
    AnimeController animeController;
    @Mock //testing the behaviour of related classes whom animeController depend on
    private AnimeService animeService;
    @BeforeEach//{Mock Anime Service Attributes}
    public void setUp(){
        //anime page
        PageImpl<Anime> animePage = new PageImpl<>(List.of(AnimeCreator.createValidAnime()));
        //list all anime find by ID
        BDDMockito.when(animeService.listAll(ArgumentMatchers.any())).thenReturn(animePage);
        //find by ID
        BDDMockito.when(animeService.findAnimeUtilById(ArgumentMatchers.anyInt())).thenReturn(AnimeCreator.createValidAnime());
        //find by name
        BDDMockito.when(animeService.findByName(ArgumentMatchers.anyString())).thenReturn(List.of(AnimeCreator.createValidAnime()));
        //saved anime
        BDDMockito.when(animeService.save(AnimeCreator.createAnimeToBeSaved())).thenReturn(AnimeCreator.createValidAnime());
        //delete anime
        BDDMockito.doNothing().when(animeService).delete(ArgumentMatchers.anyInt());
        //update anime info
        BDDMockito.when(animeService.save(AnimeCreator.createValidAnime())).thenReturn(AnimeCreator.updatedAnimeInfo());
    }
    @Test
    @DisplayName("ListAll Return A Pageable List of Animes When Successful")
    public void listAll_ReturnsListOfAnimesInsidePageObject_WhenSuccessful(){
        String expectedName = AnimeCreator.createValidAnime().getName();
        Page<Anime> animePage = animeController.animeList(null).getBody();
        Assertions.assertThat(animePage).isNotNull();
        Assertions.assertThat(animePage.toList()).isNotNull();
        Assertions.assertThat(animePage.toList().get(0).getName()).isEqualTo(expectedName);
    }
    @Test
    @DisplayName("Find By Id Returns Anime When Animes Is Successful")
    public void findById_ReturnsListOfAnimesInsidePageObject_WhenSuccessful(){
        Integer expectedId = AnimeCreator.createValidAnime().getId();
        Anime animeId = animeController.animeById(100,null).getBody();
        Assertions.assertThat(animeId).isNotNull();
        Assertions.assertThat(animeId.getId()).isNotNull();
        Assertions.assertThat(animeId.getId()).isEqualTo(expectedId);
    }
    @Test
    @DisplayName("Find By Name Returns Pageable Anime When Pageable Anime Is Successful")
    public void findByName_ReturnsListOfAnimesInsidePageObject_WhenSuccessful(){
        String animeName = AnimeCreator.createValidAnime().getName();
        List<Anime> animeList = animeController.animeByName("Monster").getBody();
        Assertions.assertThat(animeList).isNotNull();
        Assertions.assertThat(animeList).isNotEmpty();
        Assertions.assertThat(animeList.get(0).getName()).isEqualTo(animeName);
    }
    @Test
    @DisplayName("Save Creates An Anime When Successful")
    public void save_CreatesAnAnime_WhenSuccessful(){
        Integer expectedId = AnimeCreator.createValidAnime().getId();
        Anime animeToBeSaved = AnimeCreator.createAnimeToBeSaved();
        Anime anime = animeController.newEntry(AnimeCreator.createAnimeToBeSaved()).getBody();
        Assertions.assertThat(anime).isNotNull();
        Assertions.assertThat(anime.getId()).isNotNull();
        Assertions.assertThat(anime.getId()).isEqualTo(expectedId);
        Assertions.assertThat(anime.getName()).isEqualTo(animeToBeSaved.getName());
    }
    @Test
    @DisplayName("Delete Created Anime When Successful")
    public void Delete_CreatedAnime_WhenSuccessful(){
        ResponseEntity<Anime> animeResponseEntity = animeController.delete(100);
        Assertions.assertThat(animeResponseEntity).isNotNull();
        Assertions.assertThat(animeResponseEntity.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
        Assertions.assertThat(animeResponseEntity.getBody()).isNull();
    }
    @Test
    @DisplayName("Updated Anime Info Anime When Successful")
    public void updatedAnimeInfo_WhenSuccessful(){
        Anime updatedAnimeInfo = AnimeCreator.updatedAnimeInfo();
        String expectedName = updatedAnimeInfo.getName();
        ResponseEntity<Anime> animeResponseEntity = animeController.update(AnimeCreator.createValidAnime());
        Assertions.assertThat(animeResponseEntity).isNotNull();
        Assertions.assertThat(animeResponseEntity.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
        Assertions.assertThat(animeResponseEntity.getBody()).isNull();
        Assertions.assertThat(expectedName).isEqualTo(updatedAnimeInfo.getName());
    }
}