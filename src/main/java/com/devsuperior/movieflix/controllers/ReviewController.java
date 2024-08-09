package com.devsuperior.movieflix.controllers;

import com.devsuperior.movieflix.dto.ReviewDTO;
import com.devsuperior.movieflix.services.ReviewService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

import static org.springframework.web.servlet.support.ServletUriComponentsBuilder.fromCurrentRequest;

@RestController
@PreAuthorize("hasAnyRole('VISITOR','MEMBER')")
@RequestMapping(value = "/reviews")
public class ReviewController {

    @Autowired
    private ReviewService reviewService;

    @GetMapping(value = "/{id}")
    public ResponseEntity<ReviewDTO> getReviewPerId(@PathVariable Long id) {
        return ResponseEntity.ok(null);
    }

    @PostMapping
    @PreAuthorize("hasRole('MEMBER')")
    public ResponseEntity<ReviewDTO> postNewReview(@Valid @RequestBody ReviewDTO review) {
        ReviewDTO reviewDTO = reviewService.insertNewReview(review);
        URI uri = fromCurrentRequest().path("/{id}").buildAndExpand(reviewDTO.getId()).toUri();
        return ResponseEntity.created(uri).body(reviewDTO);
    }

    @PutMapping(value = "/{id}")
    public ResponseEntity<ReviewDTO> putReviewPerId(@PathVariable Long id, @Valid @RequestBody ReviewDTO reviewDTO) {
        return ResponseEntity.ok(reviewService.updateReviewPerId(id, reviewDTO));
    }

    @DeleteMapping(value = "/{id}")
    public ResponseEntity<Void> deleteReviewPerId(@PathVariable Long id) {
        reviewService.deleteReviewPerId(id);
        return ResponseEntity.noContent().build();
    }
}
