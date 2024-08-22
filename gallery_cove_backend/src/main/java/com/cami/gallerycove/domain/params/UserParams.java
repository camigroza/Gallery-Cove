package com.cami.gallerycove.domain.params;

import lombok.*;
import org.springframework.web.multipart.MultipartFile;

@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
public class UserParams {

    private String name;
    private String email;
    private String phoneNumber;
    private String password;
    private String confirmPassword;
    private MultipartFile photo;
}
