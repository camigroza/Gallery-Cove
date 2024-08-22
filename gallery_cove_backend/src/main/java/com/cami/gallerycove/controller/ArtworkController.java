package com.cami.gallerycove.controller;

import com.cami.gallerycove.domain.DTO.ArtworkDTO;
import com.cami.gallerycove.domain.DTOforFE.ArtworkDTOforFE;
import com.cami.gallerycove.domain.exception.EntityNotFoundException;
import com.cami.gallerycove.service.ArtworkImageService.ArtworkImageService;
import com.cami.gallerycove.service.ArtworkService.ArtworkService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin(origins="https://gallery-cove.onrender.com", allowedHeaders = "*")
@RequestMapping("/artwork")
public class ArtworkController {

    private final ArtworkService artworkService;

    @Autowired
    public ArtworkController(ArtworkService artworkService) {
        this.artworkService = artworkService;
    }

    @GetMapping("/getAll")
    public ResponseEntity<List<ArtworkDTO>> getAllArtworks() {
        try {
            List<ArtworkDTO> artworks = artworkService.getAllArtworks();
            return ResponseEntity.ok().body(artworks);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @GetMapping("/get/{id}")
    public ResponseEntity<ArtworkDTOforFE> getArtworkById(@PathVariable Long id) {
        try {
            ArtworkDTOforFE artwork = artworkService.getArtworkDTOforFEById(id);
            if (artwork != null) {
                return ResponseEntity.ok().body(artwork);
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @PostMapping("/add")
    public ResponseEntity<ArtworkDTOforFE> addArtwork(@RequestBody ArtworkDTO artworkDTO) {
        try {
            ArtworkDTOforFE artworkDTOforFE = artworkService.saveArtwork(artworkDTO);
            return ResponseEntity.status(HttpStatus.CREATED).body(artworkDTOforFE);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<ArtworkDTOforFE> updateArtwork(@PathVariable Long id, @RequestBody ArtworkDTO artworkDTO) {
        try {
            ArtworkDTOforFE artworkDTOforFE = artworkService.updateArtwork(id, artworkDTO);
            return ResponseEntity.ok().body(artworkDTOforFE);
        } catch (EntityNotFoundException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
    }


    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> deleteArtwork(@PathVariable Long id) {
        try {
            artworkService.deleteArtwork(id);
            return ResponseEntity.ok().body("Artwork deleted successfully!");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("ERROR:\n" + e.getMessage());
        }
    }

    @GetMapping("/getAllByUser/{userId}")
    public ResponseEntity<List<ArtworkDTOforFE>> getAllArtworksByUser(@PathVariable Long userId) {
        try {
            List<ArtworkDTOforFE> artworks = artworkService.getAllArtworksByUser(userId);
            return ResponseEntity.ok().body(artworks);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @GetMapping("/getAllByCategory/{categoryName}")
    public ResponseEntity<List<ArtworkDTOforFE>> getAllArtworksByCategory(@PathVariable String categoryName) {
        try {
            List<ArtworkDTOforFE> artworks = artworkService.getAllArtworksByCategory(categoryName);
            return ResponseEntity.ok().body(artworks);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @GetMapping("/getAllByPriceAsc")
    public ResponseEntity<List<ArtworkDTOforFE>> getAllArtworksByPriceAsc() {
        try {
            List<ArtworkDTOforFE> artworks = artworkService.getAllArtworksByPriceAsc();
            return ResponseEntity.ok().body(artworks);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @GetMapping("/getAllByPriceDesc")
    public ResponseEntity<List<ArtworkDTOforFE>> getAllArtworksByPriceDesc() {
        try {
            List<ArtworkDTOforFE> artworks = artworkService.getAllArtworksByPriceDesc();
            return ResponseEntity.ok().body(artworks);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @GetMapping("/getLatest/{count}")
    public ResponseEntity<List<ArtworkDTOforFE>> getLatestArtworks(@PathVariable Integer count) {
        try {
            List<ArtworkDTOforFE> artworks = artworkService.getAllArtworksByDateDesc();
            if (artworks.size() < count) {
                count = artworks.size();
            }
            List<ArtworkDTOforFE> result = artworks.subList(0, count);

            return ResponseEntity.ok().body(result);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @GetMapping("/DTOforFE")
    public ResponseEntity<List<ArtworkDTOforFE>> getAllArtworksForFE() {
        try {
            List<ArtworkDTOforFE> artworks = artworkService.getAllArtworksForFE();
            return ResponseEntity.ok().body(artworks);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @GetMapping("/get3Random")
    public ResponseEntity<List<ArtworkDTOforFE>> get3RandomArtworks() {
        try {
            List<ArtworkDTOforFE> artworks = artworkService.get3RandomArtworks();
            return ResponseEntity.ok().body(artworks);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @GetMapping("/getAllRandom")
    public ResponseEntity<List<ArtworkDTOforFE>> getRandomArtworks() {
        try {
            List<ArtworkDTOforFE> artworks = artworkService.getAllArtworksRandom();
            return ResponseEntity.ok().body(artworks);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @GetMapping("/getAllOldToNew")
    public ResponseEntity<List<ArtworkDTOforFE>> getArtworksOldToNew() {
        try {
            List<ArtworkDTOforFE> artworks = artworkService.getAllArtworksByDateAsc();
            return ResponseEntity.ok().body(artworks);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }
}
