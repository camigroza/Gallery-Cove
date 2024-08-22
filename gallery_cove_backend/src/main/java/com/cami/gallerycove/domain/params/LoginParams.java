package com.cami.gallerycove.domain.params;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
public class LoginParams {

    private String email;
    private String password;
    private String confirmPassword;
}
