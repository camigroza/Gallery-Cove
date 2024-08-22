package com.cami.gallerycove.domain.mapper;

import com.cami.gallerycove.domain.DTO.ArtworkImageDTO;
import com.cami.gallerycove.domain.model.ArtworkImage;
import com.cami.gallerycove.repository.ArtworkRepository;
import com.cami.gallerycove.utils.ImageUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ArtworkImageMapper {

    private final ArtworkRepository artworkRepository;

    @Autowired
    public ArtworkImageMapper(ArtworkRepository artworkRepository) {
        this.artworkRepository = artworkRepository;
    }

    public ArtworkImageDTO toDTO(ArtworkImage artworkImage) {
        ArtworkImageDTO artworkImageDTO = new ArtworkImageDTO();

        artworkImageDTO.setIdArtworkImage(artworkImage.getIdArtworkImage());
        artworkImageDTO.setArtworkId(artworkImage.getArtwork().getIdArtwork());
        artworkImageDTO.setImage(artworkImage.getImage());

        return artworkImageDTO;
    }

    public ArtworkImage toEntity(ArtworkImageDTO artworkImageDTO) {
        ArtworkImage artworkImage = new ArtworkImage();

        artworkImage.setIdArtworkImage(artworkImageDTO.getIdArtworkImage());
        artworkImage.setArtwork(artworkRepository.findById(artworkImageDTO.getArtworkId()).orElse(null));
        artworkImage.setImage(artworkImageDTO.getImage());

        return artworkImage;
    }

}
