package com.devsuperior.movieflix.services;

import com.devsuperior.movieflix.dto.ReviewDTO;
import com.devsuperior.movieflix.entities.Movie;
import com.devsuperior.movieflix.entities.Review;
import com.devsuperior.movieflix.entities.User;
import com.devsuperior.movieflix.repositories.MovieRepository;
import com.devsuperior.movieflix.repositories.ReviewRepository;
import com.devsuperior.movieflix.services.exceptions.ResourceNotFoundException;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ReviewService {

    @Autowired
    private ReviewRepository reviewRepository;

    @Autowired
    private MovieRepository movieRepository;

    @Autowired
    private UserService userService;

    @Transactional(readOnly = true)
    public List<ReviewDTO> lisAllReviews(Long movieId) {
        return reviewRepository.listAllReviewsPerMovieId(movieId);
    }

    @Transactional(readOnly = true)
    public ReviewDTO findReviewPerId(Long id) {
        return new ReviewDTO(reviewRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Id not found!")));
    }

    @Transactional
    public ReviewDTO insertNewReview(ReviewDTO reviewDTO) {
        Review review = new Review();
        converterDtoInEntity(review, reviewDTO);
        review = reviewRepository.save(review);
        return new ReviewDTO(review);
    }

    @Transactional
    public ReviewDTO updateReviewPerId(Long id, ReviewDTO reviewDTO) {
        try {
            Review review = reviewRepository.getReferenceById(id);
            converterDtoInEntity(review, reviewDTO);
            review = reviewRepository.save(review);
            return new ReviewDTO(review);
        } catch (EntityNotFoundException e) {
            throw new ResourceNotFoundException("Id not found!");
        }
    }

    @Transactional(propagation = Propagation.SUPPORTS)
    public void deleteReviewPerId(Long id) {
        if (!reviewRepository.existsById(id))
            throw new ResourceNotFoundException("Id not found!");
        try {
            reviewRepository.deleteById(id);
        } catch (DataIntegrityViolationException e) {

        }
    }

    private void converterDtoInEntity(Review review, ReviewDTO reviewDTO) {
        review.setText(reviewDTO.getText());

        Movie movie = new Movie();
        if (!movieRepository.existsById(reviewDTO.getMovieId()))
            throw new ResourceNotFoundException("Movie id not found");
        else
            movie = movieRepository.getReferenceById(reviewDTO.getMovieId());

        review.setMovie(movie);

        User user = new User(userService.getProfile());

        review.setUser(user);
    }
}
