package com.cami.gallerycove.controller;

import com.cami.gallerycove.config.JwtTokenUtil;
import com.cami.gallerycove.domain.DTOforFE.UserDTOforFE;
import com.cami.gallerycove.domain.exception.EntityNotFoundException;
import com.cami.gallerycove.domain.exception.SamePasswordException;
import com.cami.gallerycove.domain.exception.UploadFileException;
import com.cami.gallerycove.domain.params.LoginParams;
import com.cami.gallerycove.domain.model.User;
import com.cami.gallerycove.domain.params.UserParams;
import com.cami.gallerycove.service.UserService.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import org.json.JSONObject;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Objects;

@RestController
@CrossOrigin(origins="https://gallery-cove.onrender.com", allowedHeaders = "*")
@RequestMapping("/user")
public class UserController {

    private final UserService userService;
    private final JwtTokenUtil jwtTokenUtil;

    @Autowired
    public UserController(UserService userService, JwtTokenUtil jwtTokenUtil) {
        this.userService = userService;
        this.jwtTokenUtil = jwtTokenUtil;
    }

    @GetMapping("/getAll")
    public ResponseEntity<List<User>> getAllUsers() {
        try {
            List<User> users = userService.getAllUsers();
            return ResponseEntity.ok().body(users);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @GetMapping("/get/{id}")
    public ResponseEntity<UserDTOforFE> getUserById(@PathVariable Long id) {
        try {
            UserDTOforFE user = userService.getUserDTOforFEById(id);
            if (user != null) {
                return ResponseEntity.ok().body(user);
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @GetMapping("/getToken/{id}")
    public ResponseEntity<String> getUserTokenById(@PathVariable Long id) {
        try {
            User user = userService.getUserById(id);
            if (user != null) {
                final String token = jwtTokenUtil.generateToken(user);

                JSONObject responseObject = new JSONObject();
                responseObject.put("token", token);
                return ResponseEntity.ok().body(responseObject.toString());
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found!");
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    @PostMapping("/add")
    public ResponseEntity<String> addUser(@ModelAttribute UserParams params) {
        try {
            User user = new User(params.getName(), params.getEmail(), params.getPassword(), params.getPhoneNumber(), null);
            userService.saveUser(user, params.getPhoto());

            return ResponseEntity.status(HttpStatus.CREATED).body("User saved successfully!");
        } catch (UploadFileException ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("ERROR:\n" + ex.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("ERROR:\n" + e.getMessage());
        }
    }

    @PatchMapping("/update/{id}/{role}")
    public ResponseEntity<String> updateUserRole(@PathVariable Long id, @PathVariable String role) {
        try {
            userService.updateUserRole(id, role);
            return ResponseEntity.ok().body("User's role updated successfully!");
        } catch (EntityNotFoundException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("ERROR:\n" + ex.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("ERROR:\n" + e.getMessage());
        }
    }

    @PatchMapping("/updateProfile/{id}")
    public ResponseEntity<String> updateUserProfile(@PathVariable Long id, @ModelAttribute UserParams params) {
        try {
            if (params.getPhoto() != null) {
                userService.updateUserProfile(id, params.getName(), params.getPhoneNumber(), params.getPhoto());
            } else {
                userService.updateUserProfile(id, params.getName(), params.getPhoneNumber(), null);
            }

            User user = userService.getUserById(id);

            final String token = jwtTokenUtil.generateToken(user);

            JSONObject responseObject = new JSONObject();
            responseObject.put("token", token);
            return ResponseEntity.ok().body(responseObject.toString());
        } catch (EntityNotFoundException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("ERROR:\n" + ex.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("ERROR:\n" + e.getMessage());
        }
    }

    @PatchMapping("/updatePhoto/{id}")
    public ResponseEntity<String> updateUserPhoto(
            @PathVariable Long id,
            @RequestParam("image") MultipartFile file) {
        try {
            userService.updateUserPhoto(id, file);
            return ResponseEntity.ok().body("User's photo updated successfully!");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("ERROR:\n" + e.getMessage());
        }
    }

    @PatchMapping("/updatePassword")
    public ResponseEntity<String> updateUserPassword(@RequestBody LoginParams params) {
        try {
            if (! Objects.equals(params.getPassword(), params.getConfirmPassword())) {
                throw new SamePasswordException("The passwords don't match!");
            }
            userService.updateUserPassword(params.getEmail(), params.getPassword());
            return ResponseEntity.ok().body("User's password updated successfully!");
        } catch (SamePasswordException exx) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(exx.getMessage());
        } catch (EntityNotFoundException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("ERROR:\n" + ex.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("ERROR:\n" + e.getMessage());
        }
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> deleteUser(@PathVariable Long id) {
        try {
            userService.deleteUser(id);
            return ResponseEntity.ok().body("User deleted successfully!");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("ERROR:\n" + e.getMessage());
        }
    }

    @GetMapping("/getAllEmails")
    public ResponseEntity<List<String>> getAllUsersEmail() {
        try {
            List<String> emails = userService.getAllUsersEmail();
            return ResponseEntity.ok().body(emails);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @GetMapping("/DTOforFE")
    public ResponseEntity<List<UserDTOforFE>> getAllUsersForFE() {
        try {
            List<UserDTOforFE> users = userService.getAllUsersForFE();
            return ResponseEntity.ok().body(users);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @GetMapping("/getAllArtists")
    public ResponseEntity<List<UserDTOforFE>> getAllArtists() {
        try {
            List<UserDTOforFE> users = userService.getAllArtists();
            return ResponseEntity.ok().body(users);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @GetMapping("/getAllVisitors")
    public ResponseEntity<List<UserDTOforFE>> getAllVisitors() {
        try {
            List<UserDTOforFE> users = userService.getAllVisitors();
            return ResponseEntity.ok().body(users);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @PostMapping("sendEmailToAdmin")
    public ResponseEntity<String> sendEmailToAdmin(@RequestBody String message) {
        try {
            String title = "Email de la un utilizator!";
            userService.sendEmailToAdmin(title, message);
            return ResponseEntity.ok().body("Mail sent successfully!");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("ERROR:\n" + e.getMessage());
        }
    }

    @GetMapping("/getTop3Artists")
    public ResponseEntity<List<UserDTOforFE>> getTop3Artists() {
        try {
            List<UserDTOforFE> users = userService.getFirst3WithMostArtworks();
            return ResponseEntity.ok().body(users);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }
}
