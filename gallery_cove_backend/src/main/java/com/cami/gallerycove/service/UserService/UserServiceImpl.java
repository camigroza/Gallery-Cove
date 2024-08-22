package com.cami.gallerycove.service.UserService;

import com.cami.gallerycove.domain.DTOforFE.UserDTOforFE;
import com.cami.gallerycove.domain.exception.EmailNotFoundException;
import com.cami.gallerycove.domain.exception.EntityNotFoundException;
import com.cami.gallerycove.domain.exception.SamePasswordException;
import com.cami.gallerycove.domain.exception.UploadFileException;
import com.cami.gallerycove.domain.mapper.UserMapper;
import com.cami.gallerycove.domain.model.Artwork;
import com.cami.gallerycove.domain.model.MailStructure;
import com.cami.gallerycove.domain.model.User;
import com.cami.gallerycove.repository.ArtworkRepository;
import com.cami.gallerycove.repository.UserRepository;
import com.cami.gallerycove.service.MailService.MailService;
import com.cami.gallerycove.utils.ImageUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final ArtworkRepository artworkRepository;
    private final BCryptPasswordEncoder passwordEncoder;
    private final UserMapper userMapper;
    private final MailService mailService;

    @Autowired
    public UserServiceImpl(UserRepository userRepository, ArtworkRepository artworkRepository, BCryptPasswordEncoder passwordEncoder, UserMapper userMapper, MailService mailService) {
        this.userRepository = userRepository;
        this.artworkRepository = artworkRepository;
        this.passwordEncoder = passwordEncoder;
        this.userMapper = userMapper;
        this.mailService = mailService;
    }

    @Override
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    @Override
    public List<UserDTOforFE> getAllUsersForFE() {
        List<User> users = userRepository.findAll();
        return users.stream()
                .map(userMapper::toDTOforFE)
                .collect(Collectors.toList());
    }

    @Override
    public List<UserDTOforFE> getAllArtists() {
        List<User> users = userRepository.findAllByRole("artist");
        return users.stream()
                .map(userMapper::toDTOforFE)
                .collect(Collectors.toList());
    }

    @Override
    public List<UserDTOforFE> getAllVisitors() {
        List<User> users = userRepository.findAllByRole("visitor");
        return users.stream()
                .map(userMapper::toDTOforFE)
                .collect(Collectors.toList());
    }

    @Override
    public User getUserById(Long id) {
        Optional<User> optionalUser = userRepository.findById(id);
        return optionalUser.orElse(null);
    }

    @Override
    public UserDTOforFE getUserDTOforFEById(Long id) {
        Optional<User> optionalUser = userRepository.findById(id);
        return optionalUser.map(userMapper::toDTOforFE).orElse(null);
    }

    @Override
    public User getUserByEmail(String email) {
        User user = userRepository.findByEmail(email);
        if (user == null) {
            throw new EmailNotFoundException("User with email " + email + " not found");
        }
        return user;
    }


    @Override
    public void saveUser(User user, MultipartFile file) {
        String encodedPassword = passwordEncoder.encode(user.getPassword());
        user.setPassword(encodedPassword);
        try {
            user.setPhoto(ImageUtils.compressImage(file.getBytes()));
        } catch (IOException e) {
            throw new UploadFileException(e.getMessage());
        }
        userRepository.save(user);
    }

    @Override
    public void deleteUser(Long id) {
        userRepository.deleteById(id);
    }

    @Override
    public void updateUser(Long id, User updatedUser, MultipartFile file) {
        Optional<User> existingUserOptional = userRepository.findById(id);

        if (existingUserOptional.isPresent()) {
            User existingUser = existingUserOptional.get();

            existingUser.setName(updatedUser.getName());
            existingUser.setEmail(updatedUser.getEmail());
            existingUser.setPassword(updatedUser.getPassword());
            existingUser.setPhoneNumber(updatedUser.getPhoneNumber());
            existingUser.setRole(updatedUser.getRole());

            this.saveUser(existingUser, file);
        } else {
            throw new EntityNotFoundException("User with ID " + id + " not found");
        }
    }

    @Override
    public void updateUserRole(Long idUser, String updatedRole) {
        Optional<User> existingUserOptional = userRepository.findById(idUser);

        if (existingUserOptional.isPresent()) {
            User existingUser = existingUserOptional.get();
            existingUser.setRole(updatedRole);
            userRepository.save(existingUser);
        } else {
            throw new EntityNotFoundException("User with ID " + idUser + " not found");
        }
    }

    @Override
    public void updateUserPassword(String userEmail, String updatedPassword) {
        Optional<User> existingUserOptional = Optional.ofNullable(userRepository.findByEmail(userEmail));

        if (existingUserOptional.isPresent()) {
            User existingUser = existingUserOptional.get();

            if (passwordEncoder.matches(updatedPassword, existingUser.getPassword())) {
                throw new SamePasswordException("The new password needs to be different from previous one!");
            }

            existingUser.setPassword(passwordEncoder.encode(updatedPassword));
            userRepository.save(existingUser);
        } else {
            throw new EntityNotFoundException("User with ID " + userEmail + " not found");
        }
    }

    @Override
    public void updateUserProfile(Long idUser, String name, String phoneNumber, MultipartFile file) {
        Optional<User> existingUserOptional = userRepository.findById(idUser);

        if (existingUserOptional.isPresent()) {
            User existingUser = existingUserOptional.get();
            existingUser.setName(name);
            existingUser.setPhoneNumber(phoneNumber);
            if (file != null) {
                try {
                    existingUser.setPhoto(ImageUtils.compressImage(file.getBytes()));
                } catch (IOException e) {
                    throw new UploadFileException(e.getMessage());
                }
            }
            userRepository.save(existingUser);
        } else {
            throw new EntityNotFoundException("User with ID " + idUser + " not found");
        }
    }

    @Override
    public List<String> getAllUsersEmail() {
        return userRepository.findAll()
                .stream()
                .map(User::getEmail)
                .collect(Collectors.toList());
    }

    @Override
    public boolean hasArtworks(Long idUser) {
        List<Artwork> artworks = artworkRepository.findByUser(userRepository.findById(idUser).orElse(null));
        return artworks.size() != 0;
    }

    @Override
    public List<UserDTOforFE> searchUsersByQueryName(String query) {
        List<User> users = userRepository.findAll();
        List<UserDTOforFE> result = new ArrayList<>();

        String lowercaseQuery = query.toLowerCase();

        for (User user: users) {
            String lowercaseName = user.getName().toLowerCase();
            if (lowercaseName.contains(lowercaseQuery)) {
                result.add(userMapper.toDTOforFE(user));
            }
        }

        return result;
    }

    @Override
    public void sendEmailToAdmin(String title, String message) {
        MailStructure mailStructure = new MailStructure(title, message);
        mailService.sendEmail("gallerycove@gmail.com", mailStructure);
    }

    @Override
    public void updateUserPhoto(Long id, MultipartFile file) {
        Optional<User> existingUserOptional = userRepository.findById(id);

        if (existingUserOptional.isPresent()) {
            User existingUser = existingUserOptional.get();
            try {
                existingUser.setPhoto(ImageUtils.compressImage(file.getBytes()));
            } catch (IOException e) {
                throw  new UploadFileException(e.getMessage());
            }
            userRepository.save(existingUser);
        } else {
            throw new EntityNotFoundException("User with ID " + id + " not found");
        }
    }

    @Override
    public List<UserDTOforFE> getFirst3WithMostArtworks() {
        List<Object[]> results = artworkRepository.findTop3UsersByArtworkCount();
        List<UserDTOforFE> topUsers = new ArrayList<>();

        for (Object[] result : results) {
            Long userId = (Long) result[0];
            Optional<User> user = userRepository.findById(userId);
            user.ifPresent(value -> topUsers.add(userMapper.toDTOforFE(value)));
        }

        return topUsers;
    }
}
