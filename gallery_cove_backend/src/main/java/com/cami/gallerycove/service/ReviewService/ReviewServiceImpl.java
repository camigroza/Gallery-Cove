package com.cami.gallerycove.service.ReviewService;

import com.cami.gallerycove.domain.DTO.ReviewDTO;
import com.cami.gallerycove.domain.DTOforFE.ReviewDTOforFE;
import com.cami.gallerycove.domain.exception.EntityNotFoundException;
import com.cami.gallerycove.domain.mapper.ArtworkMapper;
import com.cami.gallerycove.domain.mapper.ReviewMapper;
import com.cami.gallerycove.domain.model.Artwork;
import com.cami.gallerycove.domain.model.Review;
import com.cami.gallerycove.repository.ReviewRepository;
import com.cami.gallerycove.service.ArtworkService.ArtworkService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ReviewServiceImpl implements ReviewService {

    private final ReviewRepository reviewRepository;
    private final ReviewMapper reviewMapper;
    private final ArtworkService artworkService;
    private final ArtworkMapper artworkMapper;

    @Autowired
    public ReviewServiceImpl(ReviewRepository reviewRepository, ReviewMapper reviewMapper, ArtworkService artworkService, ArtworkMapper artworkMapper) {
        this.reviewRepository = reviewRepository;
        this.reviewMapper = reviewMapper;
        this.artworkService = artworkService;
        this.artworkMapper = artworkMapper;
    }

    @Override
    public List<ReviewDTO> getAllReviews() {
        List<Review> reviews = reviewRepository.findAll();
        return reviews.stream()
                .map(reviewMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<ReviewDTOforFE> getAllReviewsForFE() {
        List<Review> reviews = reviewRepository.findAll();
        return reviews.stream()
                .map(reviewMapper::toDTOforFE)
                .collect(Collectors.toList());
    }

    @Override
    public ReviewDTO getReviewById(Long id) {
        Optional<Review> optionalReview = reviewRepository.findById(id);
        return optionalReview.map(reviewMapper::toDTO).orElse(null);
    }

    @Override
    public ReviewDTOforFE saveReview(ReviewDTO reviewDTO) {
        reviewDTO.setDate(LocalDate.now());
        reviewDTO.setTime(LocalTime.now());
        Review review = reviewRepository.save(reviewMapper.toEntity(reviewDTO));
        return reviewMapper.toDTOforFE(review);
    }

    @Override
    public void deleteReview(Long id) {
        reviewRepository.deleteById(id);
    }

    @Override
    public ReviewDTOforFE updateReview(Long id, ReviewDTO updatedReviewDTO) {
        Optional<Review> existingReviewOptional = reviewRepository.findById(id);

        if (existingReviewOptional.isPresent()) {
            Review existingReview = existingReviewOptional.get();
            Review updatedReview = reviewMapper.toEntity(updatedReviewDTO);

            existingReview.setNumberOfStars(updatedReview.getNumberOfStars());
            existingReview.setDescription(updatedReview.getDescription());
            existingReview.setUser(updatedReview.getUser());
            existingReview.setArtwork(updatedReview.getArtwork());
            existingReview.setDate(LocalDate.now());
            existingReview.setTime(LocalTime.now());

            Review review = reviewRepository.save(existingReview);
            return reviewMapper.toDTOforFE(review);
        } else {
            throw new EntityNotFoundException("Review with ID " + id + " not found");
        }
    }

    @Override
    public List<ReviewDTOforFE> getAllReviewsByArtwork(Long artworkId) {
        return reviewRepository.findByArtworkOrderByDateDescTimeDesc(artworkMapper.toEntity(artworkService.getArtworkById(artworkId)))
                .stream()
                .map(reviewMapper::toDTOforFE)
                .collect(Collectors.toList());
    }
}
