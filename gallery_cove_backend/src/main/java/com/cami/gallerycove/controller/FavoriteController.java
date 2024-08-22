package com.cami.gallerycove.controller;

import com.cami.gallerycove.domain.DTO.FavoriteDTO;
import com.cami.gallerycove.domain.DTOforFE.ArtworkDTOforFE;
import com.cami.gallerycove.service.FavoriteService.FavoriteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin(origins="https://gallery-cove.onrender.com", allowedHeaders = "*")
@RequestMapping("/favorite")
public class FavoriteController {

    private final FavoriteService favoriteService;

    @Autowired
    public FavoriteController(FavoriteService favoriteService) {
        this.favoriteService = favoriteService;
    }

    @GetMapping("/getAll")
    public ResponseEntity<List<FavoriteDTO>> getAllFavorites() {
        try {
            List<FavoriteDTO> favorites = favoriteService.getAllFavorites();
            return ResponseEntity.ok().body(favorites);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @GetMapping("/get")
    public ResponseEntity<FavoriteDTO> getFavoriteById(@RequestBody FavoriteDTO favoriteDTO) {
        try {
            FavoriteDTO favorite = favoriteService.getFavoriteById(favoriteDTO);
            if (favorite != null) {
                return ResponseEntity.ok().body(favorite);
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @PostMapping("/add")
    public ResponseEntity<String> addFavorite(@RequestBody FavoriteDTO favoriteDTO) {
        try {
            favoriteService.saveFavorite(favoriteDTO);
            return ResponseEntity.status(HttpStatus.CREATED).body("Favorite saved successfully!");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("ERROR:\n" + e.getMessage());
        }
    }

    @DeleteMapping("/delete")
    public ResponseEntity<String> deleteFavorite(@RequestBody FavoriteDTO favoriteDTO) {
        try {
            favoriteService.deleteFavorite(favoriteDTO);
            return ResponseEntity.ok().body("Favorite deleted successfully!");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("ERROR:\n" + e.getMessage());
        }
    }

    @GetMapping("/getAllByUser/{userId}")
    public ResponseEntity<List<ArtworkDTOforFE>> getAllFavoritesByUser(@PathVariable Long userId) {
        try {
            List<ArtworkDTOforFE> favorites = favoriteService.getAllFavoritesByUser(userId);
            return ResponseEntity.ok().body(favorites);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }
}
