package academy.devdojo.springboot.domain.Client;

import academy.devdojo.springboot.domain.Anime;
import academy.devdojo.springboot.domain.wrapper.PageableResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.client.RestTemplate;

@Slf4j
public class SpringClient {
    public static void main(String[] args) {
        System.out.println(new BCryptPasswordEncoder().encode("academy"));
        //Jackson Parse Data from Your End Point
        getWithRestTemplate();

        //get all anime pages
        ResponseEntity<PageableResponse<Anime>> exchangeAnimePage =
                new RestTemplate()
                        .exchange("http://localhost:8080/animes", HttpMethod.GET, null, new ParameterizedTypeReference<>() {});
        log.info("Anime Page {}", exchangeAnimePage.getBody());

        //postForObject
        Anime spaceBrothers = Anime.builder().name("Space Brothers").build();
        Anime postForObject = new RestTemplate().postForObject("http://localhost:8080/animes", spaceBrothers, Anime.class);
        assert postForObject != null;
        log.info("Space Brothers Saved id: {}", postForObject.getId());

        //Exchange
        Anime steinsGate = Anime.builder().name("Steins Gate").build();
        Anime animeExchangeNewEntry = new RestTemplate().exchange("http://localhost:8080/animes", HttpMethod.POST, new HttpEntity<>(steinsGate,createJsonHeader()), Anime.class).getBody();
        assert animeExchangeNewEntry != null;
        log.info("Steins Gate Saved Id: {}",animeExchangeNewEntry.getId());

        //put
        steinsGate.setName("Steins Gate Zero");
        ResponseEntity<Void> updatedAnimeEntry = new RestTemplate().exchange("http://localhost:8080/animes", HttpMethod.PUT, new HttpEntity<>(steinsGate, createJsonHeader()), void.class);
        log.info("Updated Anime Status Entry: {}",updatedAnimeEntry.getStatusCode());

        //delete
        //ResponseEntity<Void> deletedAnimeEntry =
          //      new RestTemplate()
            //    .exchange("http://localhost:8080/animes/{id}", HttpMethod.DELETE, null, void.class,steinsGate.getId());
        //log.info("Deleted Anime Status Entry: {}",deletedAnimeEntry.getStatusCode());
    }

    private static HttpHeaders createJsonHeader(){
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);
        return httpHeaders;
    }

    private static void getWithRestTemplate() {
        ResponseEntity<Anime> animeResponseEntity =
                new RestTemplate().getForEntity("http://localhost:8080/animes/{id}", Anime.class, 2);
        log.info("Response Entity  {}", animeResponseEntity.getBody());

        Anime anime = new RestTemplate().getForObject("http://localhost:8080/animes/{id}", Anime.class, 2);
        log.info("Get Data Object {}", anime);
    }
}
