package com.cami.gallerycove.domain.mapper;

import com.cami.gallerycove.domain.DTOforFE.UserDTOforFE;
import com.cami.gallerycove.domain.model.User;
import com.cami.gallerycove.utils.ImageUtils;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {

    public UserDTOforFE toDTOforFE(User user) {
        UserDTOforFE userDTOforFE = new UserDTOforFE();

        userDTOforFE.setIdUser(user.getIdUser());
        userDTOforFE.setName(user.getName());
        userDTOforFE.setEmail(user.getEmail());
        userDTOforFE.setPhoneNumber(user.getPhoneNumber());
        userDTOforFE.setPhoto(ImageUtils.decompressImage(user.getPhoto()));
        userDTOforFE.setRole(user.getRole());

        return userDTOforFE;
    }

}
