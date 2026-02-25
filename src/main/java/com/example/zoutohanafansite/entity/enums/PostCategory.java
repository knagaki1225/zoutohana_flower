package com.example.zoutohanafansite.entity.enums;

import java.util.Arrays;

public enum PostCategory {
    PROJECT("PROJECT", "企画情報"),
    DONATION("DONATION","寄贈情報"),
    ELSE("ELSE", "その他情報");

    private final String dbValue;
    private final String label;

    PostCategory(String dbValue, String label) {
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
    public static PostCategory fromString(String dbValue) {
        return Arrays.stream(PostCategory.values())
                .filter(e -> e.dbValue.equals(dbValue))
                .findFirst()
                .orElse(null);
    }
}
