package com.cami.gallerycove.domain.model;

import jakarta.persistence.*;

@Entity
@Table(name = "favorites")
@IdClass(FavoriteId.class)
public class Favorite {

    @Id
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @Id
    @ManyToOne
    @JoinColumn(name = "artwork_id")
    private Artwork artwork;

    //constructors, getters, setters

    public Favorite() {
    }

    public Favorite(User user, Artwork artwork) {
        this.user = user;
        this.artwork = artwork;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Artwork getArtwork() {
        return artwork;
    }

    public void setArtwork(Artwork artwork) {
        this.artwork = artwork;
    }
}

