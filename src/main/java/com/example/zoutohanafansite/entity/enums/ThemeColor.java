package com.example.zoutohanafansite.entity.enums;

import java.util.Arrays;

public enum ThemeColor {
    GREEN("#449872", "みどり"),
    PINK("#FF6C84", "もも"),
    YELLOW("#F5B84C", "き"),
    BLUE("#00BCD4", "あお"),
    BLACK("#393939", "くろ"),
    GRAY("#A0B0B7", "はい");


    private final String dbValue;
    private final String label;

    ThemeColor(String dbValue, String label) {
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
    public static ThemeColor fromString(String dbValue) {
        return Arrays.stream(ThemeColor.values())
                .filter(e -> e.dbValue.equals(dbValue))
                .findFirst()
                .orElse(null);
    }
}
