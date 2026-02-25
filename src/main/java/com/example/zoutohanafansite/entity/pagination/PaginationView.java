package com.example.zoutohanafansite.entity.pagination;

import lombok.Data;

@Data
public class PaginationView {
    private PaginationInfo paginationInfo;
    private int startNum;
    private int endNum;

    public PaginationView(PaginationInfo paginationInfo, int startNum, int endNum) {
        this.paginationInfo = paginationInfo;
        this.startNum = startNum;
        this.endNum = endNum;
    }
}
