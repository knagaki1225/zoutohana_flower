package com.example.zoutohanafansite.mapper;

import com.example.zoutohanafansite.entity.form.GenreSearchForm;
import com.example.zoutohanafansite.entity.genre.Genre;
import com.example.zoutohanafansite.entity.post.Post;
import com.example.zoutohanafansite.entity.review.Review;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface GenreMapper {
    @Select("SELECT * FROM genres WHERE deleted = false")
    List<Genre> selectAllGenre();

    // src/main/resources/mapper/GenreMapper.xml
    List<Genre> getGenreList(GenreSearchForm form);

    @Insert("""
        INSERT INTO genres (
            name,
            furigana
        ) VALUES (
            #{name},
            #{furigana}
        )
    """)
    @Options(useGeneratedKeys = true, keyProperty = "id", keyColumn = "id")
    void createGenre(Genre genre);

    @Update("UPDATE genres SET name = #{name}, furigana = #{furigana}, updated_at = NOW() WHERE id = #{id}")
    void updateGenre(Genre genre);

    @Update("UPDATE genres SET deleted = true, updated_at = NOW() WHERE id = ${id}")
    void deleteById(Long id);
}
