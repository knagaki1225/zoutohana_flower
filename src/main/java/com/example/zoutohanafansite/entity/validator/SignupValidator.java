package com.example.zoutohanafansite.entity.validator;

import lombok.Data;

@Data
public class SignupValidator {
    private boolean valid;
    private boolean nullLoginId;
    private boolean existsLoginId;
    private boolean nullNickName;
    private boolean nullAddress;
    private boolean nullBirthYear;
    private boolean nullPassword;
    private boolean nullConfirmPassword;
    private boolean passwordMismatch;

    public SignupValidator() {
        this.nullLoginId = false;
        this.existsLoginId = false;
        this.nullNickName = false;
        this.nullAddress = false;
        this.nullBirthYear = false;
        this.nullPassword = false;
        this.nullConfirmPassword = false;
        this.passwordMismatch = false;
    }

    public void updateStatus() {
        // どれか一つでも true があれば true をセット
        this.valid = (nullLoginId || existsLoginId || nullNickName ||
                nullAddress || nullBirthYear || nullPassword ||
                nullConfirmPassword || passwordMismatch);
    }
}
