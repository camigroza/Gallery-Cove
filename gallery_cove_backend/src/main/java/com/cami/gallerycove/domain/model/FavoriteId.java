package com.cami.gallerycove.domain.model;

import java.io.Serializable;
import java.util.Objects;

public class FavoriteId implements Serializable {
    private User user;
    private Artwork artwork;

    //constructors, getters, setters

    public FavoriteId() {
    }

    public FavoriteId(User user, Artwork artwork) {
        this.user = user;
        this.artwork = artwork;
    }

    public FavoriteId(Favorite favorite) {
        this.user = favorite.getUser();
        this.artwork = favorite.getArtwork();
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

    //equals, hashcode

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        FavoriteId that = (FavoriteId) o;
        return Objects.equals(user, that.user) && Objects.equals(artwork, that.artwork);
    }

    @Override
    public int hashCode() {
        return Objects.hash(user, artwork);
    }
}
