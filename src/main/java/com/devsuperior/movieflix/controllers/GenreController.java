package com.devsuperior.movieflix.controllers;

import com.devsuperior.movieflix.dto.GenreDTO;
import com.devsuperior.movieflix.services.GenreService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

import static org.springframework.web.servlet.support.ServletUriComponentsBuilder.fromCurrentRequest;

@RestController
@PreAuthorize("hasAnyRole('VISITOR', 'MEMBER')")
@RequestMapping(value = "/genres")
public class GenreController {

    @Autowired
    private GenreService genreService;

    @GetMapping
    public ResponseEntity<List<GenreDTO>> getAllGenres() {
        return ResponseEntity.ok(genreService.lisAllGenres());
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<GenreDTO> getGenrePerId(@PathVariable Long id) {
        return ResponseEntity.ok(genreService.findGenrePerId(id));
    }

    @PostMapping
    public ResponseEntity<GenreDTO> postNewGenre(@RequestBody GenreDTO genreDTO) {
        GenreDTO genre = genreService.insertNewGenre(genreDTO);
        URI uri = fromCurrentRequest().path("/{id}").buildAndExpand(genre.getId()).toUri();
        return ResponseEntity.created(uri).body(genreDTO);
    }

    @PutMapping(value = "/{id}")
    public ResponseEntity<GenreDTO> putGenrePerId(@PathVariable Long id, @RequestBody GenreDTO genreDTO) {
        return ResponseEntity.ok(genreService.updateGenrePerId(id, genreDTO));
    }

    @DeleteMapping(value = "/{id}")
    public ResponseEntity<Void> deleteGenrePerId(@PathVariable Long id) {
        genreService.deleteGenrePerId(id);
        return ResponseEntity.noContent().build();
    }
}
