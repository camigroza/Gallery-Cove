package com.cami.gallerycove.service.FavoriteService;

import com.cami.gallerycove.domain.DTO.FavoriteDTO;
import com.cami.gallerycove.domain.DTOforFE.ArtworkDTOforFE;
import com.cami.gallerycove.domain.mapper.ArtworkMapper;
import com.cami.gallerycove.domain.mapper.FavoriteMapper;
import com.cami.gallerycove.domain.model.Favorite;
import com.cami.gallerycove.domain.model.FavoriteId;
import com.cami.gallerycove.repository.FavoriteRepository;
import com.cami.gallerycove.service.UserService.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class FavoriteServiceImpl implements FavoriteService {

    private final FavoriteRepository favoriteRepository;
    private final FavoriteMapper favoriteMapper;
    private final UserService userService;
    private final ArtworkMapper artworkMapper;

    @Autowired
    public FavoriteServiceImpl(FavoriteRepository favoriteRepository, FavoriteMapper favoriteMapper, UserService userService, ArtworkMapper artworkMapper) {
        this.favoriteRepository = favoriteRepository;
        this.favoriteMapper = favoriteMapper;
        this.userService = userService;
        this.artworkMapper = artworkMapper;
    }

    @Override
    public List<FavoriteDTO> getAllFavorites() {
        List<Favorite> favorites = favoriteRepository.findAll();
        return favorites.stream()
                .map(favoriteMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public FavoriteDTO getFavoriteById(FavoriteDTO favoriteDTO) {
        FavoriteId id = new FavoriteId(favoriteMapper.toEntity(favoriteDTO));
        Optional<Favorite> optionalFavorite = favoriteRepository.findById(id);
        return optionalFavorite.map(favoriteMapper::toDTO).orElse(null);
    }

    @Override
    public void saveFavorite(FavoriteDTO favoriteDTO) {
        favoriteRepository.save(favoriteMapper.toEntity(favoriteDTO));
    }

    @Override
    public void deleteFavorite(FavoriteDTO favoriteDTO) {
        FavoriteId id = new FavoriteId(favoriteMapper.toEntity(favoriteDTO));
        favoriteRepository.deleteById(id);
    }

    @Override
    public List<ArtworkDTOforFE> getAllFavoritesByUser(Long userId) {
        return favoriteRepository.findByUser(userService.getUserById(userId))
                .stream()
                .map(favorite -> artworkMapper.toDTOforFE(favorite.getArtwork()))
                .collect(Collectors.toList());
    }
}
