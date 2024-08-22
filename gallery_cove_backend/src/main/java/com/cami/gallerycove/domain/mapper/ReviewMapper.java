package com.cami.gallerycove.domain.mapper;

import com.cami.gallerycove.domain.DTO.ReviewDTO;
import com.cami.gallerycove.domain.DTOforFE.ReviewDTOforFE;
import com.cami.gallerycove.domain.model.Review;
import com.cami.gallerycove.domain.model.User;
import com.cami.gallerycove.repository.ArtworkRepository;
import com.cami.gallerycove.repository.UserRepository;
import com.cami.gallerycove.utils.ImageUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ReviewMapper {

    private final UserRepository userRepository;
    private final ArtworkRepository artworkRepository;

    @Autowired
    public ReviewMapper(UserRepository userRepository, ArtworkRepository artworkRepository) {
        this.userRepository = userRepository;
        this.artworkRepository = artworkRepository;
    }

    public ReviewDTO toDTO(Review review) {
        ReviewDTO reviewDTO = new ReviewDTO();

        reviewDTO.setIdReview(review.getIdReview());
        reviewDTO.setNumberOfStars(review.getNumberOfStars());
        reviewDTO.setDescription(review.getDescription());
        reviewDTO.setUserId(review.getUser().getIdUser());
        reviewDTO.setArtworkId(review.getArtwork().getIdArtwork());
        reviewDTO.setDate(review.getDate());
        reviewDTO.setTime(review.getTime());

        return reviewDTO;
    }

    public Review toEntity(ReviewDTO reviewDTO) {
        Review review = new Review();

        review.setIdReview(reviewDTO.getIdReview());
        review.setNumberOfStars(reviewDTO.getNumberOfStars());
        review.setDescription(reviewDTO.getDescription());
        review.setUser(userRepository.findById(reviewDTO.getUserId()).orElse(null));
        review.setArtwork(artworkRepository.findById(reviewDTO.getArtworkId()).orElse(null));
        review.setDate(reviewDTO.getDate());
        review.setTime(reviewDTO.getTime());

        return review;
    }

    public ReviewDTOforFE toDTOforFE(Review review) {
        ReviewDTOforFE reviewDTOforFE = new ReviewDTOforFE();

        reviewDTOforFE.setIdReview(review.getIdReview());
        reviewDTOforFE.setNumberOfStars(review.getNumberOfStars());
        reviewDTOforFE.setDescription(review.getDescription());
        reviewDTOforFE.setUserName(review.getUser().getName());
        reviewDTOforFE.setArtworkId(review.getArtwork().getIdArtwork());
        reviewDTOforFE.setDate(review.getDate());
        reviewDTOforFE.setUserId(review.getUser().getIdUser());
        reviewDTOforFE.setTime(review.getTime());
        reviewDTOforFE.setUserPhoto(ImageUtils.decompressImage(review.getUser().getPhoto()));

        return reviewDTOforFE;
    }
}


