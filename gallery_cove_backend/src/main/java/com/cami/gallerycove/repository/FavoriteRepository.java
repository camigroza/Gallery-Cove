package com.cami.gallerycove.repository;

import com.cami.gallerycove.domain.model.Favorite;
import com.cami.gallerycove.domain.model.FavoriteId;
import com.cami.gallerycove.domain.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FavoriteRepository extends JpaRepository<Favorite, FavoriteId> {
    List<Favorite> findByUser(User user);
}
