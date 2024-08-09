package com.devsuperior.movieflix.controllers;

import com.devsuperior.movieflix.dto.MovieCardDTO;
import com.devsuperior.movieflix.dto.MovieDetailsDTO;
import com.devsuperior.movieflix.dto.ReviewDTO;
import com.devsuperior.movieflix.services.MovieService;
import com.devsuperior.movieflix.services.ReviewService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

import static org.springframework.web.servlet.support.ServletUriComponentsBuilder.fromCurrentRequest;

@RestController
@RequestMapping(value = "/movies")
public class MovieController {

    @Autowired
    private MovieService movieService;

    @Autowired
    private ReviewService reviewService;

    @GetMapping
    @PreAuthorize("hasAnyRole('VISITOR', 'MEMBER')")
    public ResponseEntity<Page<MovieCardDTO>> getAllMovies(Pageable pageable, @RequestParam(required = false) Long genreId) {
        return ResponseEntity.ok(movieService.lisAllMoviesPaginated(pageable, genreId));
    }

    @GetMapping(value = "/{id}")
    @PreAuthorize("hasAnyRole('VISITOR', 'MEMBER')")
    public ResponseEntity<MovieDetailsDTO> getMoviePerId(@PathVariable Long id) {
        return ResponseEntity.ok(movieService.findMovieDetailsPerId(id));
    }

    @GetMapping(value = "/{movieId}/reviews")
    @PreAuthorize("hasAnyRole('VISITOR', 'MEMBER')")
    public ResponseEntity<List<ReviewDTO>> getAllReviewsPerMovies(@PathVariable Long movieId) {
        return ResponseEntity.ok(reviewService.lisAllReviews(movieId));
    }

    @PostMapping
    @PreAuthorize("hasRole('MEMBER')")
    public ResponseEntity<MovieDetailsDTO> postNewMovie(@RequestBody MovieDetailsDTO movie) {
        MovieDetailsDTO movieDTO = movieService.insertNewMovie(movie);
        URI uri = fromCurrentRequest().path("/{id}").buildAndExpand(movieDTO.getId()).toUri();
        return ResponseEntity.created(uri).body(movieDTO);
    }

    @PutMapping(value = "/{id}")
    @PreAuthorize("hasAnyRole('MEMBER')")
    public ResponseEntity<MovieDetailsDTO> putMoviePerId(@PathVariable Long id, @RequestBody MovieDetailsDTO movieDTO) {
        return ResponseEntity.ok(movieService.updateMoviePerId(id, movieDTO));
    }

    @DeleteMapping(value = "/{id}")
    public ResponseEntity<Void> deleteMoviePerId(@PathVariable Long id) {
        movieService.deleteMoviePerId(id);
        return ResponseEntity.noContent().build();
    }
}
