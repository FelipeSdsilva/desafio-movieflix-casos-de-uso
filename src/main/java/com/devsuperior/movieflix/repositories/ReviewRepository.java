package com.devsuperior.movieflix.repositories;

import com.devsuperior.movieflix.dto.ReviewDTO;
import com.devsuperior.movieflix.entities.Review;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ReviewRepository extends JpaRepository<Review, Long > {

    @Query(name = "listAllReviewsPerMovieId")
    List<ReviewDTO> listAllReviewsPerMovieId(Long movieId);
}
