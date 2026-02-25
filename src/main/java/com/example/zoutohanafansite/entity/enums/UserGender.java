package com.example.zoutohanafansite.entity.enums;

import java.util.Arrays;

public enum UserGender {
    MALE("MALE", "男性"),
    FEMALE("FEMALE", "女性"),
    UNSPECIFIED("UNSPECIFIED", "選択しない");

    private final String dbValue;
    private final String label;

    UserGender(String dbValue, String label) {
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
    public static UserGender fromString(String dbValue) {
        return Arrays.stream(UserGender.values())
                .filter(e -> e.dbValue.equals(dbValue))
                .findFirst()
                .orElse(null);
    }
}
