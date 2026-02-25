package com.example.zoutohanafansite.entity.pagination;

import lombok.Data;

@Data
public class PaginationInfo {
    private int current;
    private int total;

    public PaginationInfo(int current, int total) {
        this.current = current;
        this.total = total;
    }
}
