package com.cami.gallerycove.service.ReviewService;

import com.cami.gallerycove.domain.DTO.ReviewDTO;
import com.cami.gallerycove.domain.DTOforFE.ReviewDTOforFE;

import java.util.List;

public interface ReviewService {
    List<ReviewDTO> getAllReviews();

    List<ReviewDTOforFE> getAllReviewsForFE();

    ReviewDTO getReviewById(Long id);

    ReviewDTOforFE saveReview(ReviewDTO reviewDTO);

    void deleteReview(Long id);

    ReviewDTOforFE updateReview(Long id, ReviewDTO updatedReviewDTO);

    List<ReviewDTOforFE> getAllReviewsByArtwork(Long artworkId);
}
