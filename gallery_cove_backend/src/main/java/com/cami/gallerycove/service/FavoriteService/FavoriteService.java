package com.cami.gallerycove.service.FavoriteService;

import com.cami.gallerycove.domain.DTO.FavoriteDTO;
import com.cami.gallerycove.domain.DTOforFE.ArtworkDTOforFE;

import java.util.List;

public interface FavoriteService {
    List<FavoriteDTO> getAllFavorites();

    FavoriteDTO getFavoriteById(FavoriteDTO favoriteDTO);

    void saveFavorite(FavoriteDTO favoriteDTO);

    void deleteFavorite(FavoriteDTO favoriteDTO);

    List<ArtworkDTOforFE> getAllFavoritesByUser(Long userId);

}
