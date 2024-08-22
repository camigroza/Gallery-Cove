package com.cami.gallerycove.domain.params;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
public class VerifyCodeParams {

    private String email;
    private Integer code;
}
