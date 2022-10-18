package academy.devdojo.springboot.domain.animeCreator;

import academy.devdojo.springboot.domain.Anime;

public class AnimeCreator {
    public static Anime createAnimeToBeSaved() {return Anime.builder().name("Monster").build();}
    public static Anime createValidAnime(){return Anime.builder().name("Monster").id(100).build();}
    public static Anime updatedAnimeInfo(){return Anime.builder().name("Monster 2").id(100).build();}
}
