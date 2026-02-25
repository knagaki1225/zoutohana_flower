package com.example.zoutohanafansite.service;

import com.example.zoutohanafansite.entity.enums.PostCategory;
import com.example.zoutohanafansite.entity.enums.PostStatus;
import com.example.zoutohanafansite.entity.form.PostSearchForm;
import com.example.zoutohanafansite.entity.pagination.PaginationInfo;
import com.example.zoutohanafansite.entity.pagination.PaginationView;
import com.example.zoutohanafansite.entity.post.Post;
import com.example.zoutohanafansite.entity.post.PostPagination;
import com.example.zoutohanafansite.entity.post.PostTop;
import com.example.zoutohanafansite.repository.PostRepository;
import org.springframework.cglib.core.Local;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class PostService {
    private final PostRepository postRepository;
    private final PaginationService paginationService;

    public PostService(PostRepository postRepository, PaginationService paginationService) {
        this.postRepository = postRepository;
        this.paginationService = paginationService;
    }

    /**
     * 公開しているお知らせ全件取得
     *
     * @return List<Post>
     */
    public List<Post> getPublicPosts(){
        return postRepository.selectPublicPosts();
    }

    /**
     * トップページ表示用新規お知らせ全件取得
     *
     * @return List<PostTop>
     */
    public List<PostTop> getNew(){
        List<PostTop> result = new ArrayList<PostTop>();
        List<Post> posts = getPublicPosts();
        for(Post post : posts){
            result.add(new PostTop(post));
        }
        return result;
    }

    /**
     * カテゴリー指定でのお知らせ全件取得
     *
     * @param category カテゴリー名
     * @return List<Post>
     */
    public List<Post> getPostByCategory(String category){
        return postRepository.selectPublicPostsByCategory(category);
    }

    /**
     * カテゴリー指定でのトップページ用お知らせ全件取得
     *
     * @param category カテゴリー名
     * @return List<PostTop>
     */
    public List<PostTop> getPostTopByCategory(String category){
        List<Post> posts = getPostByCategory(category);
        List<PostTop> result = new ArrayList<>();
        for(Post post : posts){
            result.add(new PostTop(post));
        }
        return result;
    }

    /**
     * ページネーション情報付きトップページ用お知らせ取得
     *
     * @param category カテゴリー名(新規お知らせの場合は"new")
     * @param page 取得ページ数
     * @return PostPagination
     */
    public PostPagination getPostPagination(String category, int page){
        List<PostTop> posts;
        if(category.equals("new")){
            posts = getNew();
        }else{
            String searchStr = category.toUpperCase();

            try{
                PostCategory.valueOf(searchStr);
            }catch(IllegalArgumentException e){
                // エラー
            }

            posts = getPostTopByCategory(searchStr);
        }

        if(posts.isEmpty()){
            return null;
        }

        List<PostTop> result = new ArrayList<>();
        PaginationView paginationView = paginationService.getPaginationView(page, posts.size(), 6);
        PaginationInfo paginationInfo = paginationView.getPaginationInfo();

        for(int i = paginationView.getStartNum(); i < paginationView.getEndNum(); i++){
            PostTop post = posts.get(i);
            result.add(post);
        }

        return new PostPagination(paginationInfo, result);
    }

    /**
     * 管理者用お知らせ全件取得
     *
     * @param form PostSearchForm(検索条件)
     *              String sort, keyword
     *              List<String> status, category
     *              LocalDateTime createdStartAt, createdEndAt, postedStartAt, postedEndAt
     * @return List<Post>
     */
    public List<Post> getAllPosts(PostSearchForm form){
        return postRepository.selectAllPosts(form);
    }

    /**
     * 管理者用ID指定お知らせ取得
     *
     * @param id お知らせID
     * @return Post
     */
    public Post getPostById(long id){
        return postRepository.selectPostById(id);
    }


    /**
     * ID指定でのお知らせ取得
     *
     * @param id お知らせID
     * @return Post
     */
    public Post getPublicPostById(long id){
        return postRepository.selectPublicPostById(id);
    }

    /**
     * お知らせタイトル検索
     *
     * @param keyword 検索ワード
     * @return List<Post>
     */
    public List<Post> getPostsByKeyword(String keyword){
        return postRepository.selectPostsByKeyword(keyword);
    }

    public List<PostTop> getPostTopsByKeyword(String keyword){
        List<PostTop> result = new ArrayList<>();
        List<Post> posts = getPostsByKeyword(keyword);
        for(Post post : posts){
            result.add(new PostTop(post));
        }
        return result;
    }

    /**
     * お知らせ削除
     *
     * @param id お知らせID
     * @return boolean
     */
    public boolean deletePostById(long id){
        return postRepository.deletePostById(id);
    }

    /**
     * お知らせ更新
     *
     * @param post 上書きするお知らせの内容
     * @return boolean
     */
    public boolean updatePost(Post post){
        return postRepository.updatePost(post);
    }

    /**
     * お知らせ作成
     *
     * @param post 作成するお知らせの内容
     * @return Post 作成したお知らせ
     */
    public Post createPost(Post post) {
        post.setStatus(PostStatus.valueOf("DRAFT"));
        post.setPostedAt(LocalDateTime.now());
        post.setUpdatedAt(LocalDateTime.now());
        postRepository.createPost(post);
        return post;
    }
}
