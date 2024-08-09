package com.devsuperior.movieflix.services;

import com.devsuperior.movieflix.dto.MovieCardDTO;
import com.devsuperior.movieflix.dto.MovieDetailsDTO;
import com.devsuperior.movieflix.entities.Genre;
import com.devsuperior.movieflix.entities.Movie;
import com.devsuperior.movieflix.repositories.GenreRepository;
import com.devsuperior.movieflix.repositories.MovieRepository;
import com.devsuperior.movieflix.services.exceptions.ResourceNotFoundException;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
public class MovieService {

    @Autowired
    private MovieRepository movieRepository;

    @Autowired
    private GenreRepository genreRepository;

    @Transactional(readOnly = true)
    public Page<MovieCardDTO> lisAllMoviesPaginated(Pageable pageable, Long genreId) {
        return movieRepository.listAllMoviesPaginatedOrdPerTitleOrGenre(pageable, genreId);
    }

    @Transactional(readOnly = true)
    public MovieDetailsDTO findMovieDetailsPerId(Long id) {
        return new MovieDetailsDTO(movieRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Id not found!")));
    }

    @Transactional
    public MovieDetailsDTO insertNewMovie(MovieDetailsDTO movieDetailsDTO) {
        Movie movie = new Movie();
        converterDtoInEntity(movie, movieDetailsDTO);
        movie = movieRepository.save(movie);
        return new MovieDetailsDTO(movie);
    }

    @Transactional
    public MovieDetailsDTO updateMoviePerId(Long id, MovieDetailsDTO movieDetailsDTO) {
        try {
            Movie movie = movieRepository.getReferenceById(id);
            converterDtoInEntity(movie, movieDetailsDTO);
            movie = movieRepository.save(movie);
            return new MovieDetailsDTO(movie);
        } catch (EntityNotFoundException e) {
            throw new ResourceNotFoundException("Id not found!");
        }
    }

    @Transactional(propagation = Propagation.SUPPORTS)
    public void deleteMoviePerId(Long id) {
        if (!movieRepository.existsById(id))
            throw new ResourceNotFoundException("Id not found!");
        try {
            movieRepository.deleteById(id);
        } catch (DataIntegrityViolationException e) {

        }
    }

    private void converterDtoInEntity(Movie movie, MovieDetailsDTO movieDetailsDTO) {
        movie.setTitle(movieDetailsDTO.getTitle());
        movie.setSubTitle(movieDetailsDTO.getSubTitle());
        movie.setYear(movieDetailsDTO.getYear());
        movie.setImgUrl(movieDetailsDTO.getImgUrl());
        movie.setSynopsis(movieDetailsDTO.getSynopsis());

        if (!genreRepository.existsById(movieDetailsDTO.getGenre().getId()))
            throw new ResourceNotFoundException("Genre id not found!");
        Genre genre = genreRepository.getReferenceById(movieDetailsDTO.getGenre().getId());

        movie.setGenre(genre);
    }
}
