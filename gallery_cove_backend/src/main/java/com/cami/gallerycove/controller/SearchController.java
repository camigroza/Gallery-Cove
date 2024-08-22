package com.cami.gallerycove.controller;

import com.cami.gallerycove.domain.DTOforFE.ArtworkDTOforFE;
import com.cami.gallerycove.domain.DTOforFE.UserDTOforFE;
import com.cami.gallerycove.service.ArtworkService.ArtworkService;
import com.cami.gallerycove.service.UserService.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin(origins="https://gallery-cove.onrender.com", allowedHeaders = "*")
@RequestMapping("/search")
public class SearchController {

    private final UserService userService;
    private final ArtworkService artworkService;

    @Autowired
    public SearchController(UserService userService, ArtworkService artworkService) {
        this.userService = userService;
        this.artworkService = artworkService;
    }

    @GetMapping("/users")
    public ResponseEntity<List<UserDTOforFE>> globalUserSearch(@RequestParam String query) {
        try {
            List<UserDTOforFE> users = userService.searchUsersByQueryName(query);
            return ResponseEntity.ok().body(users);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @GetMapping("/artworks")
    public ResponseEntity<List<ArtworkDTOforFE>> globalArtworkSearch(@RequestParam String query) {
        try {
            List<ArtworkDTOforFE> artworks = artworkService.searchArtworksByQueryTitle(query);
            return ResponseEntity.ok().body(artworks);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }
}
