package com.cami.gallerycove.domain.mapper;

import com.cami.gallerycove.domain.DTO.ArtworkDTO;
import com.cami.gallerycove.domain.DTOforFE.ArtworkDTOforFE;
import com.cami.gallerycove.domain.model.Artwork;
import com.cami.gallerycove.domain.model.ArtworkImage;
import com.cami.gallerycove.repository.ArtworkImageRepository;
import com.cami.gallerycove.repository.CategoryRepository;
import com.cami.gallerycove.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

@Component
public class ArtworkMapper {

    private final UserRepository userRepository;
    private final CategoryRepository categoryRepository;
    private final ArtworkImageRepository artworkImageRepository;

    @Autowired
    public ArtworkMapper(UserRepository userRepository, CategoryRepository categoryRepository, ArtworkImageRepository artworkImageRepository) {
        this.userRepository = userRepository;
        this.categoryRepository = categoryRepository;
        this.artworkImageRepository = artworkImageRepository;
    }

    public ArtworkDTO toDTO(Artwork artwork) {
        ArtworkDTO artworkDTO = new ArtworkDTO();

        artworkDTO.setIdArtwork(artwork.getIdArtwork());
        artworkDTO.setUserId(artwork.getUser().getIdUser());
        artworkDTO.setDescription(artwork.getDescription());
        artworkDTO.setPrice(artwork.getPrice());
        artworkDTO.setCategoryName(artwork.getCategory().getName());
        artworkDTO.setDate(artwork.getDate());
        artworkDTO.setTitle(artwork.getTitle());

        return artworkDTO;
    }

    public Artwork toEntity(ArtworkDTO artworkDTO) {
        Artwork artwork = new Artwork();

        artwork.setIdArtwork(artworkDTO.getIdArtwork());
        artwork.setUser(userRepository.findById(artworkDTO.getUserId()).orElse(null));
        artwork.setDescription(artworkDTO.getDescription());
        artwork.setPrice(artworkDTO.getPrice());
        artwork.setCategory(categoryRepository.findCategoryByName(artworkDTO.getCategoryName()).orElse(null));
        artwork.setDate(artworkDTO.getDate());
        artwork.setTitle(artworkDTO.getTitle());

        return artwork;
    }

    public ArtworkDTOforFE toDTOforFE(Artwork artwork) {
        ArtworkDTOforFE artworkDTOforFE = new ArtworkDTOforFE();

        artworkDTOforFE.setIdArtwork(artwork.getIdArtwork());
        artworkDTOforFE.setUserName(artwork.getUser().getName());
        artworkDTOforFE.setDescription(artwork.getDescription());
        artworkDTOforFE.setPrice(artwork.getPrice());
        artworkDTOforFE.setCategoryName(artwork.getCategory().getName());
        artworkDTOforFE.setDate(artwork.getDate());
        artworkDTOforFE.setIdUser(artwork.getUser().getIdUser());
        artworkDTOforFE.setTitle(artwork.getTitle());

        artworkDTOforFE.setPhotos(artworkImageRepository.findByArtwork(artwork)
                .stream()
                .map(ArtworkImage::getImage).toList());

        return artworkDTOforFE;
    }
}
