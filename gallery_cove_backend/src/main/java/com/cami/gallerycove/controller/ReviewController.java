package com.cami.gallerycove.controller;

import com.cami.gallerycove.domain.DTO.ReviewDTO;
import com.cami.gallerycove.domain.DTOforFE.ReviewDTOforFE;
import com.cami.gallerycove.domain.exception.EntityNotFoundException;
import com.cami.gallerycove.service.ReviewService.ReviewService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin(origins="https://gallery-cove.onrender.com", allowedHeaders = "*")
@RequestMapping("/review")
public class ReviewController {

    private final ReviewService reviewService;

    @Autowired
    public ReviewController(ReviewService reviewService) {
        this.reviewService = reviewService;
    }

    @GetMapping("/getAll")
    public ResponseEntity<List<ReviewDTO>> getAllReviews() {
        try {
            List<ReviewDTO> reviews = reviewService.getAllReviews();
            return ResponseEntity.ok().body(reviews);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @GetMapping("/get/{id}")
    public ResponseEntity<ReviewDTO> getReviewById(@PathVariable Long id) {
        try {
            ReviewDTO review = reviewService.getReviewById(id);
            if (review != null) {
                return ResponseEntity.ok().body(review);
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @PostMapping("/add")
    public ResponseEntity<ReviewDTOforFE> addReview(@RequestBody ReviewDTO reviewDTO) {
        try {
            ReviewDTOforFE reviewDTOforFE = reviewService.saveReview(reviewDTO);
            return ResponseEntity.status(HttpStatus.CREATED).body(reviewDTOforFE);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<ReviewDTOforFE> updateReview(@PathVariable Long id, @RequestBody ReviewDTO reviewDTO) {
        try {
            ReviewDTOforFE reviewDTOforFE = reviewService.updateReview(id, reviewDTO);
            return ResponseEntity.ok().body(reviewDTOforFE);
        } catch (EntityNotFoundException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> deleteReview(@PathVariable Long id) {
        try {
            reviewService.deleteReview(id);
            return ResponseEntity.ok().body("Review deleted successfully!");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("ERROR:\n" + e.getMessage());
        }
    }

    @GetMapping("/getAllByArtwork/{artworkId}")
    public ResponseEntity<List<ReviewDTOforFE>> getAllReviewsByArtwork(@PathVariable Long artworkId) {
        try {
            List<ReviewDTOforFE> reviews = reviewService.getAllReviewsByArtwork(artworkId);
            return ResponseEntity.ok().body(reviews);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @GetMapping("/DTOforFE")
    public ResponseEntity<List<ReviewDTOforFE>> getAllReviewsForFE() {
        try {
            List<ReviewDTOforFE> reviews = reviewService.getAllReviewsForFE();
            return ResponseEntity.ok().body(reviews);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }
}
