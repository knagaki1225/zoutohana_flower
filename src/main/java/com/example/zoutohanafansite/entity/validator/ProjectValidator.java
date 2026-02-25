package com.example.zoutohanafansite.entity.validator;

import lombok.Data;

@Data
public class ProjectValidator {
    private boolean valid;
    private boolean nullName;
    private boolean nullUrlKey;
    private boolean existsUrlKey;
    private boolean nullIntroduction;
    private boolean nullOngoingAt;
    private boolean nullSubmissionAt;
    private boolean nullVotingAt;
    private boolean nullThemeColor;

    public ProjectValidator(){
        this.valid = false;
        this.nullName = false;
        this.nullUrlKey = false;
        this.existsUrlKey = false;
        this.nullIntroduction = false;
        this.nullOngoingAt = false;
        this.nullSubmissionAt = false;
        this.nullVotingAt = false;
        this.nullThemeColor = false;
    }

    public void updateStatus(){
        this.valid = (
                nullName || nullUrlKey || existsUrlKey || nullIntroduction || nullOngoingAt || nullSubmissionAt || nullVotingAt || nullThemeColor
        );
    }
}
