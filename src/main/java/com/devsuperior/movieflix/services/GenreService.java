package com.devsuperior.movieflix.services;

import com.devsuperior.movieflix.dto.GenreDTO;
import com.devsuperior.movieflix.entities.Genre;
import com.devsuperior.movieflix.repositories.GenreRepository;
import com.devsuperior.movieflix.services.exceptions.ResourceNotFoundException;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class GenreService {

    @Autowired
    private GenreRepository genreRepository;

    @Transactional(readOnly = true)
    public List<GenreDTO> lisAllGenres() {
        return genreRepository.findAll().stream().map(GenreDTO::new).toList();
    }

    @Transactional(readOnly = true)
    public GenreDTO findGenrePerId(Long id) {
        return new GenreDTO(genreRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Id not found!")));
    }

    @Transactional
    public GenreDTO insertNewGenre(GenreDTO genreDTO) {
        Genre genre = new Genre();
        genre.setName(genreDTO.getName());
        genre = genreRepository.save(genre);
        return new GenreDTO(genre);
    }

    @Transactional
    public GenreDTO updateGenrePerId(Long id, GenreDTO genreDTO) {
        try {
            Genre genre = genreRepository.getReferenceById(id);
            genre.setName(genreDTO.getName());
            genre = genreRepository.save(genre);
            return new GenreDTO(genre);
        } catch (EntityNotFoundException e) {
            throw new ResourceNotFoundException("Id not found!");
        }
    }

    @Transactional(propagation = Propagation.SUPPORTS)
    public void deleteGenrePerId(Long id) {
        if (!genreRepository.existsById(id))
            throw new ResourceNotFoundException("Id not found!");
        try {
            genreRepository.deleteById(id);
        } catch (DataIntegrityViolationException e) {
        }
    }
}
