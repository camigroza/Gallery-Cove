package com.cami.gallerycove.service.ResetPasswordCodeService;

import com.cami.gallerycove.domain.model.ResetPasswordCode;

public interface ResetPasswordCodeService {

    ResetPasswordCode getResetPasswordCode(String email);

    int generateCode(String email);

    void deleteCode(String email);

    boolean verifyCode(String email, int code);
}
