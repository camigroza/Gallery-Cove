package com.cami.gallerycove.domain.mapper;

import com.cami.gallerycove.domain.DTO.FavoriteDTO;
import com.cami.gallerycove.domain.model.Favorite;
import com.cami.gallerycove.repository.ArtworkRepository;
import com.cami.gallerycove.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class FavoriteMapper {

    private final UserRepository userRepository;
    private final ArtworkRepository artworkRepository;

    @Autowired
    public FavoriteMapper(UserRepository userRepository, ArtworkRepository artworkRepository) {
        this.userRepository = userRepository;
        this.artworkRepository = artworkRepository;
    }

    public FavoriteDTO toDTO(Favorite favorite) {
        FavoriteDTO favoriteDTO = new FavoriteDTO();

        favoriteDTO.setUserId(favorite.getUser().getIdUser());
        favoriteDTO.setArtworkId(favorite.getArtwork().getIdArtwork());

        return favoriteDTO;
    }

    public Favorite toEntity(FavoriteDTO favoriteDTO) {
        Favorite favorite = new Favorite();

        favorite.setUser(userRepository.findById(favoriteDTO.getUserId()).orElse(null));
        favorite.setArtwork(artworkRepository.findById(favoriteDTO.getArtworkId()).orElse(null));

        return favorite;
    }
}

