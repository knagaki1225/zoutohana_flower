package com.example.zoutohanafansite.repository;

import com.example.zoutohanafansite.entity.auth.AdminUser;
import com.example.zoutohanafansite.entity.form.PostSearchForm;
import com.example.zoutohanafansite.entity.post.Post;
import com.example.zoutohanafansite.entity.post.PostTop;
import com.example.zoutohanafansite.mapper.PostMapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class PostRepository {
    private final PostMapper postMapper;

    public PostRepository(PostMapper postMapper) {
        this.postMapper = postMapper;
    }

    public List<Post> selectPublicPosts() {
        return postMapper.selectPublicPosts();
    }

    public List<Post> selectPublicPostsByCategory(String category){
        return postMapper.selectPublicPostsByCategory(category);
    }

    public Post selectPublicPostById(long id){
        return postMapper.selectPublicPostById(id);
    }

    public List<Post> selectPostsByKeyword(String keyword){
        return postMapper.selectPostsByKeyword(keyword);
    }

    public List<Post> selectAllPosts(PostSearchForm form) {
        return postMapper.getAllPosts(form);
    }

    public Post selectPostById(long id){
        return postMapper.selectPostById(id);
    }

    public boolean deletePostById(long id){
        return postMapper.deletePostById(id);
    }

    public boolean updatePost(Post post) {
        return postMapper.updatePost(post);
    }

    public void createPost(Post post) {
        postMapper.createPost(post);
    }
}
