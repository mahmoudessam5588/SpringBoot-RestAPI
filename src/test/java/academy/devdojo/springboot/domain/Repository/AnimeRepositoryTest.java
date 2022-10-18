package academy.devdojo.springboot.domain.Repository;


import academy.devdojo.springboot.domain.Anime;
import academy.devdojo.springboot.domain.animeCreator.AnimeCreator;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import javax.validation.ConstraintViolationException;
import java.util.List;
import java.util.Optional;


@DataJpaTest
@DisplayName("Anime Repository Tests")
public class AnimeRepositoryTest {
    @Autowired
    AnimeRepository animeRepository;

    @Test
    @DisplayName("Save And Display Anime When Successful")
    public void save_PersistenceAnime_WhenSuccessful() {
        Anime anime = AnimeCreator.createAnimeToBeSaved();
        Anime savedAnime = this.animeRepository.save(anime);
        //Assert J
        Assertions.assertThat(savedAnime.getId()).isNotNull();
        Assertions.assertThat(savedAnime.getName()).isNotNull();
        Assertions.assertThat(savedAnime.getName()).isEqualTo(anime.getName());
    }

    @Test
    @DisplayName("Save Updated Anime Info When Successful")
    public void save_UpdatedAnime_WhenSuccessful() {
        Anime anime = AnimeCreator.createAnimeToBeSaved();
        Anime savedAnime = this.animeRepository.save(anime);
        savedAnime.setName("Diamond No Ace");
        Anime updatedAnime = this.animeRepository.save(savedAnime);
        //Assert J
        Assertions.assertThat(savedAnime.getId()).isNotNull();
        Assertions.assertThat(savedAnime.getName()).isNotNull();
        Assertions.assertThat(savedAnime.getName()).isEqualTo(updatedAnime.getName());
    }

    @Test
    @DisplayName("Delete removes Anime When Successful")
    public void delete_RemovesAnime_WhenSuccessful() {
        Anime anime = AnimeCreator.createAnimeToBeSaved();
        Anime savedAnime = this.animeRepository.save(anime);
        this.animeRepository.delete(savedAnime);
        Optional<Anime> animeOptional = this.animeRepository.findById(savedAnime.getId());
        //Assert J
        Assertions.assertThat(animeOptional.isEmpty()).isTrue();
    }

    @Test
    @DisplayName("Find By Name Returns Anime When Successful")
    public void findByName_ReturnsAnime_WhenSuccessful() {
        Anime anime = AnimeCreator.createAnimeToBeSaved();
        Anime savedAnime = this.animeRepository.save(anime);
        String name = savedAnime.getName();
        List<Anime> animeList = this.animeRepository.findByName(name);
        //Assert J
        Assertions.assertThat(animeList).isNotEmpty();
        Assertions.assertThat(animeList).contains(savedAnime);
    }

    @Test
    @DisplayName("Find By Anime Returns Empty List When No Anime Is Found")
    public void findAnime_ReturnsEmptyList_WhenAnimeNotFound() {
        String name = "Fake-Name";
        List<Anime> animeList = this.animeRepository.findByName(name);
        Assertions.assertThat(animeList).isEmpty();
    }

    @Test
    @DisplayName("Save Throw ConstraintViolation Exception When Name Is Empty")
    public void save_ThrowConstraintViolationException_WhenAnimeNameIsEmpty() {
        Anime anime = new Anime();
        //Assertions.assertThatThrownBy(() -> animeRepository.save(anime)).isInstanceOf(ConstraintViolationException.class);
        Assertions.assertThatExceptionOfType(ConstraintViolationException.class).isThrownBy(()->animeRepository.save(anime));
    }
}