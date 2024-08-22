package com.cami.gallerycove.repository;

import com.cami.gallerycove.domain.model.Artwork;
import com.cami.gallerycove.domain.model.ArtworkImage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ArtworkImageRepository extends JpaRepository<ArtworkImage,Long> {
    List<ArtworkImage> findByArtwork(Artwork artwork);
}
