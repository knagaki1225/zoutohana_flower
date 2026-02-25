package com.example.zoutohanafansite.entity.enums;

import java.util.Arrays;

public enum ProjectStatus {
    BEFORE_SUBMISSION("BEFORE_SUBMISSION", "書評受付前", 1),
    DURING_SUBMISSION("DURING_SUBMISSION", "書評受付中", 2),
    FIRST_PHASE("FIRST_PHASE", "一次審査中", 3),
    SECOND_PHASE_VOTING("SECOND_PHASE_VOTING", "投票受付中",4),
    SECOND_PHASE_VERIFY("SECOND_PHASE_VERIFY", "投票結果確認中", 5),
    SECOND_PHASE_RESULT("SECOND_PHASE_RESULT", "投票結果発表", 6),
    AWARD_ANNOUNCEMENT("AWARD_ANNOUNCEMENT", "大賞発表", 7);

    private final String dbValue;
    private final String label;
    private final int order;

    ProjectStatus(String dbValue, String label, int order) {
        this.dbValue = dbValue;
        this.label = label;
        this.order = order;
    }

    public String getDbValue() {
        return dbValue;
    }

    public String getLabel() {
        return label;
    }

    public int getOrder() {
        return order;
    }

    //    データベースの文字列から Enum を取得
    public static ProjectStatus fromString(String dbValue) {
        return Arrays.stream(ProjectStatus.values())
                .filter(e -> e.dbValue.equals(dbValue))
                .findFirst()
                .orElse(null);
    }
}
