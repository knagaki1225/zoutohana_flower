package com.example.zoutohanafansite.entity.enums;

import java.util.Arrays;

public enum PostStatus {
    DRAFT("DRAFT", "下書き"),
    PRIVATE("PRIVATE", "非公開"),
    PUBLIC("PUBLIC", "公開");

    private final String dbValue;
    private final String label;

    PostStatus(String dbValue, String label) {
        this.dbValue = dbValue;
        this.label = label;
    }

    public String getDbValue() {
        return dbValue;
    }

    public String getLabel() {
        return label;
    }

    //    データベースの文字列から Enum を取得
    public static PostStatus fromString(String dbValue) {
        return Arrays.stream(PostStatus.values())
                .filter(e -> e.dbValue.equals(dbValue))
                .findFirst()
                .orElse(null);
    }
}
