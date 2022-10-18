package academy.devdojo.springboot.domain.Integration;

import academy.devdojo.springboot.domain.Anime;
import academy.devdojo.springboot.domain.Repository.AnimeRepository;
import academy.devdojo.springboot.domain.animeCreator.AnimeCreator;
import academy.devdojo.springboot.domain.wrapper.PageableResponse;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.BDDMockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Lazy;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.*;

import java.util.List;
import java.util.Optional;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class AnimeControllerIT {
    @Autowired
    @Qualifier(value = "testRestTemplateRoleUser")
    private TestRestTemplate testRestTemplateRoleUser;

    @Autowired
    @Qualifier(value = "testRestTemplateRoleAdminCreator")
    private TestRestTemplate testRestTemplateRoleAdmin;
    @MockBean
    private AnimeRepository animeRepository;

    @Lazy
    @TestConfiguration
    static class config {
        @Bean(name = "testRestTemplateRoleUser")
        public TestRestTemplate testRestTemplateRoleUserCreator(@Value("${local.server.port}") int port) {
            RestTemplateBuilder restTemplateBuilder =
                    new RestTemplateBuilder()
                            .rootUri("http://localhost:" + port).basicAuthentication("devdojo", "academy");
            return new TestRestTemplate(restTemplateBuilder);
        }

        @Bean(name = "testRestTemplateRoleAdminCreator")
        public TestRestTemplate testRestTemplateRoleAdminCreator(@Value("${local.server.port}") int port) {
            RestTemplateBuilder restTemplateBuilder =
                    new RestTemplateBuilder()
                            .rootUri("http://localhost:" + port).basicAuthentication("mahmoud", "academy");
            return new TestRestTemplate(restTemplateBuilder);
        }

    }

    @BeforeEach//{Mock Anime Repository + Utils Attributes}
    public void setUp() {
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
    }

    @Test
    @DisplayName("ListAll Return A Pageable List of Animes When Successful")
    public void listAll_ReturnsListOfAnimesInsidePageObject_WhenSuccessful() {
        String expectedName = AnimeCreator.createValidAnime().getName();
        //@formatter:off
        Page<Anime> animePage = testRestTemplateRoleUser
                .exchange("/animes", HttpMethod.GET, null, new ParameterizedTypeReference<PageableResponse<Anime>>() {
                }).getBody();
        //@formatter:on
        Assertions.assertThat(animePage).isNotNull();
        Assertions.assertThat(animePage.toList()).isNotNull();
        Assertions.assertThat(animePage.toList().get(0).getName()).isEqualTo(expectedName);
    }

    @Test
    @DisplayName("Find By Id Returns Anime When Animes Is Successful")
    public void findById_ReturnsListOfAnimesInsidePageObject_WhenSuccessful() {
        Integer expectedId = AnimeCreator.createValidAnime().getId();
        Anime animeId = testRestTemplateRoleUser.getForObject("/animes/100", Anime.class);
        Assertions.assertThat(animeId).isNotNull();
        Assertions.assertThat(animeId.getId()).isNotNull();
        Assertions.assertThat(animeId.getId()).isEqualTo(expectedId);
    }

    @Test
    @DisplayName("Find By Name Returns Pageable Anime When Pageable Anime Is Successful")
    public void findByName_ReturnsListOfAnimesInsidePageObject_WhenSuccessful() {
        String animeName = AnimeCreator.createValidAnime().getName();
        List<Anime> animeList = testRestTemplateRoleUser
                .exchange("/animes/find?name='Monster'", HttpMethod.GET, null, new ParameterizedTypeReference<List<Anime>>() {
                }).getBody();
        Assertions.assertThat(animeList).isNotNull();
        Assertions.assertThat(animeList).isNotEmpty();
        Assertions.assertThat(animeList.get(0).getName()).isEqualTo(animeName);
    }

    @Test
    @DisplayName("Save Creates An Anime When Successful")
    public void save_CreatesAnAnime_WhenSuccessful() {
        Integer expectedId = AnimeCreator.createValidAnime().getId();
        Anime animeToBeSaved = AnimeCreator.createAnimeToBeSaved();
        Anime anime = testRestTemplateRoleUser.exchange("/animes", HttpMethod.POST, createJsonHttpEntity(animeToBeSaved), Anime.class).getBody();
        Assertions.assertThat(anime).isNotNull();
        Assertions.assertThat(anime.getId()).isNotNull();
        Assertions.assertThat(anime.getId()).isEqualTo(expectedId);
        Assertions.assertThat(anime.getName()).isEqualTo(animeToBeSaved.getName());
    }

    @Test
    @DisplayName("Delete Created Anime When Successful By Admin USer Role")
    public void Delete_CreatedAnime_WhenSuccessful_ByAdminUserRole() {
        ResponseEntity<Void> animeResponseEntity = testRestTemplateRoleAdmin.exchange("/animes/admin/100", HttpMethod.DELETE, null, void.class);
        Assertions.assertThat(animeResponseEntity).isNotNull();
        Assertions.assertThat(animeResponseEntity.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
        Assertions.assertThat(animeResponseEntity.getBody()).isNull();
    }

    @Test
    @DisplayName("Delete Created Anime Forbidden By  USer Role")
    public void Delete_CreatedAnime_Forbidden_ByUserRole() {
        ResponseEntity<Void> animeResponseEntity = testRestTemplateRoleUser.exchange("/animes/admin/100", HttpMethod.DELETE, null, void.class);
        Assertions.assertThat(animeResponseEntity).isNotNull();
        Assertions.assertThat(animeResponseEntity.getStatusCode()).isEqualTo(HttpStatus.FORBIDDEN);
        Assertions.assertThat(animeResponseEntity.getBody()).isNull();
    }

    @Test
    @DisplayName("Updated Anime Info Anime When Successful")
    public void updatedAnimeInfo_WhenSuccessful() {
        Anime updatedAnimeInfo = AnimeCreator.updatedAnimeInfo();
        String expectedName = updatedAnimeInfo.getName();
        ResponseEntity<Void> animeResponseEntity = testRestTemplateRoleUser.exchange("/animes", HttpMethod.PUT, createJsonHttpEntity(updatedAnimeInfo), void.class);
        Assertions.assertThat(animeResponseEntity).isNotNull();
        Assertions.assertThat(animeResponseEntity.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
        Assertions.assertThat(animeResponseEntity.getBody()).isNull();
        Assertions.assertThat(expectedName).isEqualTo(updatedAnimeInfo.getName());
    }

    private static HttpHeaders createJsonHeader() {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);
        return httpHeaders;
    }

    private HttpEntity<Anime> createJsonHttpEntity(Anime anime) {
        return new HttpEntity<>(anime, createJsonHeader());
    }

}