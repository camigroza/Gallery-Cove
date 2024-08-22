package com.cami.gallerycove.domain.DTOforFE;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
public class UserDTOforFE {

    private Long idUser;
    private String name;
    private String email;
    private String phoneNumber;
    private byte[] photo;
    private String role;
}
