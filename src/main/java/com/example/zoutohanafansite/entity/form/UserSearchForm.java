package com.example.zoutohanafansite.entity.form;

import lombok.Data;

import java.util.List;

@Data
public class UserSearchForm {
    private String sort;
    private String keyword;

    private List<String> status;
    private List<String> gender;
    private List<String> ageGroup;
    private List<String> address;
}
