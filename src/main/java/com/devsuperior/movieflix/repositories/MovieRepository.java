package com.devsuperior.movieflix.repositories;

import com.devsuperior.movieflix.dto.MovieCardDTO;
import com.devsuperior.movieflix.entities.Movie;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface MovieRepository extends JpaRepository<Movie, Long> {

    @Query(name = "listAllMoviesPaginatedOrdPerTitleOrGenre")
    Page<MovieCardDTO> listAllMoviesPaginatedOrdPerTitleOrGenre(Pageable pageable, Long genreId);
}
