package com.cami.gallerycove.controller;

import com.cami.gallerycove.config.JwtTokenUtil;
import com.cami.gallerycove.domain.exception.SamePasswordException;
import com.cami.gallerycove.domain.params.LoginParams;
import com.cami.gallerycove.domain.exception.EmailNotFoundException;
import com.cami.gallerycove.domain.model.User;
import com.cami.gallerycove.domain.params.UserParams;
import com.cami.gallerycove.service.UserService.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Objects;

@RestController
@CrossOrigin(origins="https://gallery-cove.onrender.com", allowedHeaders = "*")
@RequestMapping("/api/auth")
public class AuthController {

    private final JwtTokenUtil jwtTokenUtil;
    private final UserService userService;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public AuthController(JwtTokenUtil jwtTokenUtil, UserService userService, PasswordEncoder passwordEncoder) {
        this.jwtTokenUtil = jwtTokenUtil;
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
    }

    @PostMapping("/login")
    public ResponseEntity<String> authenticateUser(@RequestBody LoginParams params) {
        try {
            User user = userService.getUserByEmail(params.getEmail());
            if (passwordEncoder.matches(params.getPassword(), user.getPassword())) {
                final String token = jwtTokenUtil.generateToken(user);
                return ResponseEntity.ok(token);
            } else {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Parola incorecta!");
            }
        } catch (EmailNotFoundException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("ERROR:\n" + ex.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("ERROR:\n" + e.getMessage());
        }
    }

    @PostMapping("/register")
    public ResponseEntity<String> registerUser(@ModelAttribute UserParams params) {
        try {
            User existingUser = userService.getUserByEmail(params.getEmail());
            if (existingUser != null) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("There already is a user with this email!");
            } else {
                User user = new User(params.getName(), params.getEmail(), params.getPassword(), params.getPhoneNumber(), null);
                userService.saveUser(user, params.getPhoto());

                return ResponseEntity.ok("Utilizator inregistrat cu succes!");
            }
        } catch (EmailNotFoundException e) {
            try {
                if (! Objects.equals(params.getPassword(), params.getConfirmPassword())) {
                    throw new SamePasswordException("The passwords don't match!");
                }
                User user = new User(params.getName(), params.getEmail(), params.getPassword(), params.getPhoneNumber(), null);
                userService.saveUser(user, params.getPhoto());

                return ResponseEntity.ok("Utilizator inregistrat cu succes!");
            } catch (SamePasswordException exx) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(exx.getMessage());
            } catch (Exception ex) {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("ERROR:\n" + ex.getMessage());
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("ERROR:\n" + e.getMessage());
        }
    }

}
