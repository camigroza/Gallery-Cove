package com.cami.gallerycove.repository;

import com.cami.gallerycove.domain.DTOforFE.UserDTOforFE;
import com.cami.gallerycove.domain.model.Artwork;
import com.cami.gallerycove.domain.model.Category;
import com.cami.gallerycove.domain.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ArtworkRepository extends JpaRepository<Artwork, Long> {
    List<Artwork> findByUser(User user);

    List<Artwork> findByCategory(Category category);

    List<Artwork> findByOrderByPriceAsc();

    List<Artwork> findByOrderByPriceDesc();

    List<Artwork> findByOrderByDateDesc();

    List<Artwork> findByOrderByDateAsc();

    @Query(value = "SELECT a.user_id, COUNT(a.id_artwork) as artwork_count " +
            "FROM artworks a " +
            "GROUP BY a.user_id " +
            "ORDER BY artwork_count DESC " +
            "LIMIT 3", nativeQuery = true)
    List<Object[]> findTop3UsersByArtworkCount();

    @Query(value = "SELECT * FROM artworks ORDER BY RAND() LIMIT 3", nativeQuery = true)
    List<Artwork> findRandom3Artworks();

    @Query(value = "SELECT * FROM artworks ORDER BY RAND()", nativeQuery = true)
    List<Artwork> findRandomArtworks();
}
