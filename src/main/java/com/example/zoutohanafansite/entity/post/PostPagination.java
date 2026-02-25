package com.example.zoutohanafansite.entity.post;

import com.example.zoutohanafansite.entity.pagination.PaginationInfo;
import lombok.Data;

import java.util.List;

@Data
public class PostPagination {
    private PaginationInfo paginationInfo;
    private List<PostTop> posts;

    public PostPagination(PaginationInfo paginationInfo, List<PostTop> postTop) {
        this.paginationInfo = paginationInfo;
        this.posts = postTop;
    }
}
