package com.cami.gallerycove.controller;

import com.cami.gallerycove.domain.DTO.ArtworkImageDTO;
import com.cami.gallerycove.domain.params.ArtworkImageParams;
import com.cami.gallerycove.service.ArtworkImageService.ArtworkImageService;
import com.cami.gallerycove.utils.ImageUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@CrossOrigin(origins="https://gallery-cove.onrender.com", allowedHeaders = "*")
@RequestMapping("/artwork_image")
public class ArtworkImageController {

    private final ArtworkImageService artworkImageService;

    @Autowired
    public ArtworkImageController(ArtworkImageService artworkImageService) {
        this.artworkImageService = artworkImageService;
    }

    @GetMapping("/getAll")
    public ResponseEntity<List<ArtworkImageDTO>> getAllArtworkImages() {
        try {
            List<ArtworkImageDTO> artworkImages = artworkImageService.getAllArtworkImages();
            return ResponseEntity.ok().body(artworkImages);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @GetMapping("/get/{id}")
    public ResponseEntity<ArtworkImageDTO> getArtworkImage(@PathVariable Long id) {
        try {
            ArtworkImageDTO artworkImage = artworkImageService.getArtworkImageById(id);
            if (artworkImage != null) {
                return ResponseEntity.ok().body(artworkImage);
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @PostMapping("/add")
    public ResponseEntity<String> addArtworkImage(@ModelAttribute ArtworkImageParams params) {
        try {
            ArtworkImageDTO artworkImageDTO = new ArtworkImageDTO();
            artworkImageDTO.setArtworkId(params.getArtworkId());
            artworkImageDTO.setImage(params.getPhoto().getBytes());

            artworkImageService.saveArtworkImage(artworkImageDTO);
            return ResponseEntity.status(HttpStatus.CREATED).body("ArtworkImage saved successfully!");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("ERROR:\n" + e.getMessage());
        }
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> deleteArtworkImage(@PathVariable Long id) {
        try {
            artworkImageService.deleteArtworkImage(id);
            return ResponseEntity.ok().body("ArtworkImage deleted successfully!");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("ERROR:\n" + e.getMessage());
        }
    }

    @DeleteMapping("/deleteAllImagesOfArtwork/{idArtwork}")
    public ResponseEntity<String> deleteAllImagesOfArtwork(@PathVariable Long idArtwork) {
        try {
            artworkImageService.deleteAllImagesByArtwork(idArtwork);
            return ResponseEntity.ok().body("Artwork's images deleted successfully!");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("ERROR:\n" + e.getMessage());
        }
    }

    @GetMapping("/getAllByArtwork/{artworkId}")
    public ResponseEntity<List<ArtworkImageDTO>> getAllArtworkImagesByArtwork(@PathVariable Long artworkId) {
        try {
            List<ArtworkImageDTO> artworkImages = artworkImageService.getAllArtworkImagesByArtwork(artworkId);
            return ResponseEntity.ok().body(artworkImages);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }
}
