package com.example.zoutohanafansite.repository;

import com.example.zoutohanafansite.entity.form.GenreSearchForm;
import com.example.zoutohanafansite.entity.genre.Genre;
import com.example.zoutohanafansite.mapper.GenreMapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class GenreRepository {
    private final GenreMapper genreMapper;

    public GenreRepository(GenreMapper genreMapper) {
        this.genreMapper = genreMapper;
    }

    public List<Genre> selectAllGenre() {
        return genreMapper.selectAllGenre();
    }

    public List<Genre> getGenreList(GenreSearchForm form) {
        return genreMapper.getGenreList(form);
    }

    public void createGenre(Genre genre) {
        genreMapper.createGenre(genre);
    }

    public void updateGenre(Genre genre) {
        genreMapper.updateGenre(genre);
    }

    public void deleteById(Long id) {
        genreMapper.deleteById(id);
    }
}
