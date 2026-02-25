package com.example.zoutohanafansite.entity.enums;

import java.util.Arrays;

public enum UserStatus {
    ACTIVE("ACTIVE", "有効"),
    SUSPENDED("SUSPENDED", "停止"),
    BAN("BAN", "無効");

    private final String dbValue;
    private final String label;

    UserStatus(String dbValue, String label) {
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
    public static UserStatus fromString(String dbValue) {
        return Arrays.stream(UserStatus.values())
                .filter(e -> e.dbValue.equals(dbValue))
                .findFirst()
                .orElse(null);
    }
}
