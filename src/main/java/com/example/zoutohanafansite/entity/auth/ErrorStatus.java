package com.example.zoutohanafansite.entity.auth;

import lombok.Data;

@Data
public class ErrorStatus {
    private String status;
    private String message;

    public ErrorStatus(String status, String message) {
        this.status = status;
        this.message = message;
    }
}
