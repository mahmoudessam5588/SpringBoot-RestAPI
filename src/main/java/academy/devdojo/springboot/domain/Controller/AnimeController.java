package academy.devdojo.springboot.domain.Controller;

import academy.devdojo.springboot.domain.Anime;
import academy.devdojo.springboot.domain.Service.AnimeService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("animes")
@Slf4j
public class AnimeController {
    private final AnimeService animeService;

    @GetMapping
    public ResponseEntity<Page<Anime>> animeList(Pageable pageable) {return ResponseEntity.ok(animeService.listAll(pageable));}

    @GetMapping(path = "/{id}")
    public ResponseEntity<Anime> animeById(@PathVariable Integer id, @AuthenticationPrincipal UserDetails userDetails) {log.info("Logged User {}",userDetails);return  ResponseEntity.ok(animeService.findAnimeUtilById(id));}

    @GetMapping(path = "/find")
    public ResponseEntity<List<Anime>> animeByName(@RequestParam(name = "name") String name) {return ResponseEntity.ok(animeService.findByName(name));}

    @PostMapping
    public  ResponseEntity<Anime> newEntry(@RequestBody @Valid Anime anime){return ResponseEntity.ok(animeService.save(anime));}

    @DeleteMapping("/admin/{id}")
    public ResponseEntity<Anime>delete(@PathVariable Integer id) {animeService.delete(id);return new ResponseEntity<>(HttpStatus.NO_CONTENT);}

    @PutMapping
    public ResponseEntity<Anime>update(@RequestBody Anime anime){animeService.replace(anime);return new ResponseEntity<>(HttpStatus.NO_CONTENT);}

}
