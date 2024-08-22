package com.cami.gallerycove.domain.model;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "reset_password_codes")
public class ResetPasswordCode {

    @Id
    @Column(name = "email")
    private String email;

    @Column(name = "verification_code")
    private int verificationCode;

    @Column(name = "expire_time")
    private LocalDateTime expireTime;

    //constructors, getters, setters

    public ResetPasswordCode() {
    }

    public ResetPasswordCode(String email, int verificationCode, LocalDateTime expireTime) {
        this.email = email;
        this.verificationCode = verificationCode;
        this.expireTime = expireTime;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public int getVerificationCode() {
        return verificationCode;
    }

    public void setVerificationCode(int verificationCode) {
        this.verificationCode = verificationCode;
    }

    public LocalDateTime getExpireTime() {
        return expireTime;
    }

    public void setExpireTime(LocalDateTime expireTime) {
        this.expireTime = expireTime;
    }
}
