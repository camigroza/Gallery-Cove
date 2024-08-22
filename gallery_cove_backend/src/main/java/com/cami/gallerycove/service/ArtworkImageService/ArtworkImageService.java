package com.cami.gallerycove.service.ArtworkImageService;

import com.cami.gallerycove.domain.DTO.ArtworkImageDTO;

import java.util.List;

public interface ArtworkImageService {
    List<ArtworkImageDTO> getAllArtworkImages();

    ArtworkImageDTO getArtworkImageById(Long id);

    void saveArtworkImage(ArtworkImageDTO artworkImageDTO);

    void deleteArtworkImage(Long id);

    List<ArtworkImageDTO> getAllArtworkImagesByArtwork(Long artworkId);

    void deleteAllImagesByArtwork(Long idArtwork);
}
