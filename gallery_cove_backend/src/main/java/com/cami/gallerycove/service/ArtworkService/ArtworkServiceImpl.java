package com.cami.gallerycove.service.ArtworkService;

import com.cami.gallerycove.domain.DTO.ArtworkDTO;
import com.cami.gallerycove.domain.DTOforFE.ArtworkDTOforFE;
import com.cami.gallerycove.domain.exception.CategoryNotFoundException;
import com.cami.gallerycove.domain.exception.EntityNotFoundException;
import com.cami.gallerycove.domain.mapper.ArtworkMapper;
import com.cami.gallerycove.domain.model.Artwork;
import com.cami.gallerycove.domain.model.User;
import com.cami.gallerycove.repository.ArtworkRepository;
import com.cami.gallerycove.service.ArtworkImageService.ArtworkImageService;
import com.cami.gallerycove.service.CategoryService.CategoryService;
import com.cami.gallerycove.service.UserService.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ArtworkServiceImpl implements ArtworkService {

    private final ArtworkRepository artworkRepository;
    private final ArtworkMapper artworkMapper;
    private final UserService userService;
    private final CategoryService categoryService;

    @Autowired
    public ArtworkServiceImpl(ArtworkRepository artworkRepository, ArtworkMapper artworkMapper, UserService userService, CategoryService categoryService) {
        this.artworkRepository = artworkRepository;
        this.artworkMapper = artworkMapper;
        this.userService = userService;
        this.categoryService = categoryService;
    }

    @Override
    public List<ArtworkDTO> getAllArtworks() {
        List<Artwork> artworks = artworkRepository.findAll();
        return artworks.stream()
                .map(artworkMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<ArtworkDTOforFE> getAllArtworksForFE() {
        List<Artwork> artworks = artworkRepository.findByOrderByDateDesc();
        return artworks.stream()
                .map(artworkMapper::toDTOforFE)
                .collect(Collectors.toList());
    }

    @Override
    public ArtworkDTO getArtworkById(Long id) {
        Optional<Artwork> optionalArtwork = artworkRepository.findById(id);
        return optionalArtwork.map(artworkMapper::toDTO).orElse(null);
    }

    @Override
    public ArtworkDTOforFE getArtworkDTOforFEById(Long id) {
        Optional<Artwork> optionalArtwork = artworkRepository.findById(id);
        return optionalArtwork.map(artworkMapper::toDTOforFE).orElse(null);
    }

    @Override
    public ArtworkDTOforFE saveArtwork(ArtworkDTO artworkDTO) {
        Artwork artwork = artworkMapper.toEntity(artworkDTO);
        if (artwork.getCategory() == null) {
            throw new CategoryNotFoundException("Category \"" + artworkDTO.getCategoryName() + "\" doesn't exist!");
        }

        artworkDTO.setDate(LocalDate.now());
        Artwork artwork1 =  artworkRepository.save(artworkMapper.toEntity(artworkDTO));

        User user = userService.getUserById(artworkDTO.getUserId());
        if (Objects.equals(user.getRole(), "visitor")) {
            userService.updateUserRole(user.getIdUser(), "artist");
        }

        return artworkMapper.toDTOforFE(artwork1);
    }

    @Override
    public void deleteArtwork(Long id) {
        Optional<Artwork> optionalArtwork = artworkRepository.findById(id);
        artworkRepository.deleteById(id);

        if (optionalArtwork.isPresent()) {
            Artwork artwork = optionalArtwork.get();
            Long idUser = artwork.getUser().getIdUser();
            if (! userService.hasArtworks(idUser)) {
                userService.updateUserRole(idUser, "visitor");
            }
        }
    }

    @Override
    public ArtworkDTOforFE updateArtwork(Long id, ArtworkDTO updatedArtworkDTO) {
        Optional<Artwork> existingArtworkOptional = artworkRepository.findById(id);

        if (existingArtworkOptional.isPresent()) {
            Artwork existingArtwork = existingArtworkOptional.get();
            Artwork updatedArtwork = artworkMapper.toEntity(updatedArtworkDTO);

            existingArtwork.setUser(updatedArtwork.getUser());
            existingArtwork.setDescription(updatedArtwork.getDescription());
            existingArtwork.setPrice(updatedArtwork.getPrice());
            existingArtwork.setCategory(updatedArtwork.getCategory());
            existingArtwork.setDate(LocalDate.now());
            existingArtwork.setTitle(updatedArtwork.getTitle());

            Artwork artwork = artworkRepository.save(existingArtwork);
            return artworkMapper.toDTOforFE(artwork);
        } else {
            throw new EntityNotFoundException("Artwork with ID " + id + " not found");
        }
    }

    @Override
    public List<ArtworkDTOforFE> getAllArtworksByUser(Long userId) {
        return artworkRepository.findByUser(userService.getUserById(userId))
                .stream()
                .map(artworkMapper::toDTOforFE)
                .collect(Collectors.toList());
    }

    @Override
    public List<ArtworkDTOforFE> getAllArtworksByCategory(String name) {
        return artworkRepository.findByCategory(categoryService.getCategoryByName(name))
                .stream()
                .map(artworkMapper::toDTOforFE)
                .collect(Collectors.toList());
    }

    @Override
    public List<ArtworkDTOforFE> getAllArtworksByPriceAsc() {
        return artworkRepository.findByOrderByPriceAsc()
                .stream()
                .map(artworkMapper::toDTOforFE)
                .collect(Collectors.toList());
    }

    @Override
    public List<ArtworkDTOforFE> getAllArtworksByPriceDesc() {
        return artworkRepository.findByOrderByPriceDesc()
                .stream()
                .map(artworkMapper::toDTOforFE)
                .collect(Collectors.toList());
    }

    @Override
    public List<ArtworkDTOforFE> getAllArtworksByDateDesc() {
        return artworkRepository.findByOrderByDateDesc()
                .stream()
                .map(artworkMapper::toDTOforFE)
                .collect(Collectors.toList());
    }

    @Override
    public List<ArtworkDTOforFE> getAllArtworksByDateAsc() {
        return artworkRepository.findByOrderByDateAsc()
                .stream()
                .map(artworkMapper::toDTOforFE)
                .collect(Collectors.toList());
    }

    @Override
    public List<ArtworkDTOforFE> searchArtworksByQueryTitle(String query) {
        List<Artwork> artworks = artworkRepository.findAll();
        List<ArtworkDTOforFE> result = new ArrayList<>();

        String lowercaseQuery = query.toLowerCase();

        for (Artwork artwork: artworks) {
            String lowercaseTitle = artwork.getTitle().toLowerCase();
            if (lowercaseTitle.contains(lowercaseQuery)) {
                result.add(artworkMapper.toDTOforFE(artwork));
            }
        }

        return result;
    }

    @Override
    public List<ArtworkDTOforFE> get3RandomArtworks() {
        return artworkRepository.findRandom3Artworks()
                .stream()
                .map(artworkMapper::toDTOforFE)
                .collect(Collectors.toList());
    }

    @Override
    public List<ArtworkDTOforFE> getAllArtworksRandom() {
        return artworkRepository.findRandomArtworks()
                .stream()
                .map(artworkMapper::toDTOforFE)
                .collect(Collectors.toList());
    }
}
