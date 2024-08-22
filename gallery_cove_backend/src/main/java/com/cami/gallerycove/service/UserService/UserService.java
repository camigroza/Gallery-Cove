package com.cami.gallerycove.service.UserService;

import com.cami.gallerycove.domain.DTOforFE.UserDTOforFE;
import com.cami.gallerycove.domain.model.User;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface UserService {
    List<User> getAllUsers();

    List<UserDTOforFE> getAllUsersForFE();

    List<UserDTOforFE> getAllArtists();

    List<UserDTOforFE> getAllVisitors();

    User getUserById(Long id);

    UserDTOforFE getUserDTOforFEById(Long id);

    User getUserByEmail(String email);

    void saveUser(User user, MultipartFile file);

    void deleteUser(Long id);

    void updateUser(Long id, User updatedUser, MultipartFile file);

    void updateUserRole(Long idUser, String updatedRole);

    void updateUserPassword(String userEmail, String updatedPassword);

    void updateUserProfile(Long idUser, String name, String phoneNumber, MultipartFile file);

    List<String> getAllUsersEmail();

    boolean hasArtworks(Long idUser);

    List<UserDTOforFE> searchUsersByQueryName(String query);

    void sendEmailToAdmin(String title, String message);

    void updateUserPhoto(Long id, MultipartFile file);

    List<UserDTOforFE> getFirst3WithMostArtworks();
}
