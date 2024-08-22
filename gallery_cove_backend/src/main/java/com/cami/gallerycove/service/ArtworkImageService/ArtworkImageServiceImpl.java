package com.cami.gallerycove.service.ArtworkImageService;

import com.cami.gallerycove.domain.DTO.ArtworkImageDTO;
import com.cami.gallerycove.domain.mapper.ArtworkImageMapper;
import com.cami.gallerycove.domain.mapper.ArtworkMapper;
import com.cami.gallerycove.domain.model.ArtworkImage;
import com.cami.gallerycove.repository.ArtworkImageRepository;
import com.cami.gallerycove.service.ArtworkService.ArtworkService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ArtworkImageServiceImpl implements ArtworkImageService {

    private final ArtworkImageRepository artworkImageRepository;
    private final ArtworkImageMapper artworkImageMapper;
    private final ArtworkService artworkService;
    private final ArtworkMapper artworkMapper;

    @Autowired
    public ArtworkImageServiceImpl(ArtworkImageRepository artworkImageRepository, ArtworkImageMapper artworkImageMapper, ArtworkService artworkService, ArtworkMapper artworkMapper) {
        this.artworkImageRepository = artworkImageRepository;
        this.artworkImageMapper = artworkImageMapper;
        this.artworkService = artworkService;
        this.artworkMapper = artworkMapper;
    }

    @Override
    public List<ArtworkImageDTO> getAllArtworkImages() {
        List<ArtworkImage> artworkImages = artworkImageRepository.findAll();
        return artworkImages.stream()
                .map(artworkImageMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public ArtworkImageDTO getArtworkImageById(Long id) {
        Optional<ArtworkImage> optionalArtworkImage = artworkImageRepository.findById(id);
        return optionalArtworkImage.map(artworkImageMapper::toDTO).orElse(null);
    }

    @Override
    public void saveArtworkImage(ArtworkImageDTO artworkImageDTO) {
        artworkImageRepository.save(artworkImageMapper.toEntity(artworkImageDTO));
    }

    @Override
    public void deleteArtworkImage(Long id) {
        artworkImageRepository.deleteById(id);
    }

    @Override
    public List<ArtworkImageDTO> getAllArtworkImagesByArtwork(Long artworkId) {
        return artworkImageRepository.findByArtwork(artworkMapper.toEntity(artworkService.getArtworkById(artworkId)))
                .stream()
                .map(artworkImageMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public void deleteAllImagesByArtwork(Long idArtwork) {
        List<ArtworkImage> artworkImageList = artworkImageRepository.findByArtwork(artworkMapper.toEntity(artworkService.getArtworkById(idArtwork)));
        for (ArtworkImage artworkImage: artworkImageList) {
            artworkImageRepository.deleteById(artworkImage.getIdArtworkImage());
        }
    }
}
