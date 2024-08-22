package com.cami.gallerycove.repository;

import com.cami.gallerycove.domain.model.Artwork;
import com.cami.gallerycove.domain.model.Review;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReviewRepository extends JpaRepository<Review, Long> {
    List<Review> findByArtworkOrderByDateDescTimeDesc(Artwork artwork);
}
