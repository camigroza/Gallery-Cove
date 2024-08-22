package com.cami.gallerycove.service.ArtworkService;

import com.cami.gallerycove.domain.DTO.ArtworkDTO;
import com.cami.gallerycove.domain.DTOforFE.ArtworkDTOforFE;

import java.util.List;

public interface ArtworkService {
    List<ArtworkDTO> getAllArtworks();

    List<ArtworkDTOforFE> getAllArtworksForFE();

    ArtworkDTO getArtworkById(Long id);

    ArtworkDTOforFE getArtworkDTOforFEById(Long id);

    ArtworkDTOforFE saveArtwork(ArtworkDTO artworkDTO);

    void deleteArtwork(Long id);

    ArtworkDTOforFE updateArtwork(Long id, ArtworkDTO updatedArtworkDTO);

    List<ArtworkDTOforFE> getAllArtworksByUser(Long userId);

    List<ArtworkDTOforFE> getAllArtworksByCategory(String name);

    List<ArtworkDTOforFE> getAllArtworksByPriceAsc();

    List<ArtworkDTOforFE> getAllArtworksByPriceDesc();

    List<ArtworkDTOforFE> getAllArtworksByDateDesc();

    List<ArtworkDTOforFE> getAllArtworksByDateAsc();

    List<ArtworkDTOforFE> searchArtworksByQueryTitle(String query);

    List<ArtworkDTOforFE> get3RandomArtworks();

    List<ArtworkDTOforFE> getAllArtworksRandom();
}
