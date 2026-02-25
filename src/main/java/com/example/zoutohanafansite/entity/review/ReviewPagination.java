package com.example.zoutohanafansite.entity.review;

import com.example.zoutohanafansite.entity.pagination.PaginationInfo;
import lombok.Data;

import java.util.List;

@Data
public class ReviewPagination {
    private PaginationInfo info;
    private List<ReviewApiData> reviews;

    public ReviewPagination(PaginationInfo info, List<ReviewApiData> reviews) {
        this.info = info;
        this.reviews = reviews;
    }
}
