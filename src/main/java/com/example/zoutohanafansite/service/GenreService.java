package com.example.zoutohanafansite.service;

import com.example.zoutohanafansite.entity.enums.PostStatus;
import com.example.zoutohanafansite.entity.form.GenreSearchForm;
import com.example.zoutohanafansite.entity.genre.Genre;
import com.example.zoutohanafansite.entity.post.Post;
import com.example.zoutohanafansite.repository.GenreRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class GenreService {
    private final GenreRepository genreRepository;

    public GenreService(GenreRepository genreRepository) {
        this.genreRepository = genreRepository;
    }

    /**
     * ジャンル全取得
     *
     * @return List<Genre>
     */
    public List<Genre> selectAllGenre() {
        return genreRepository.selectAllGenre();
    }

    /**
     * ジャンル一覧用ジャンル全取得
     *
     * @param form GenreSearchForm(検索条件)
     *              String sort, keyword
     * @return List<Genre>
     */
    public List<Genre> getGenreList(GenreSearchForm form) {
        return genreRepository.getGenreList(form);
    }

    /**
     * ジャンル作成
     *
     * @param genre
     */
    public void createGenre(Genre genre) {
        genre.setCreatedAt(LocalDateTime.now());
        genre.setUpdatedAt(LocalDateTime.now());
        genreRepository.createGenre(genre);
    }

    /**
     * ジャンル更新
     *
     * @param genre
     */
    @Transactional
    public void updateGenre(Genre genre) {
        genreRepository.updateGenre(genre);
    }

    /**
     * ジャンル削除
     *
     * @param id 削除するジャンルのID
     */
    public void deleteById(Long id) {
        genreRepository.deleteById(id);
    }
}
