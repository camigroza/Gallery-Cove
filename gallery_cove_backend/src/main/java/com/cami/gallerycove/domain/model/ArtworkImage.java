package com.cami.gallerycove.domain.model;


import jakarta.persistence.*;

@Entity
@Table(name = "artwork_images")
public class ArtworkImage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_artwork_image")
    private Long idArtworkImage;

    @ManyToOne
    @JoinColumn(name = "artwork_id")
    private Artwork artwork;

    @Column(name = "image")
    private byte[] image;

    //constructors, getters, setters

    public ArtworkImage() {
    }

    public ArtworkImage(Long idArtworkImage, Artwork artwork, byte[] image) {
        this.idArtworkImage = idArtworkImage;
        this.artwork = artwork;
        this.image = image;
    }

    public Long getIdArtworkImage() {
        return idArtworkImage;
    }

    public void setIdArtworkImage(Long idArtworkImage) {
        this.idArtworkImage = idArtworkImage;
    }

    public Artwork getArtwork() {
        return artwork;
    }

    public void setArtwork(Artwork artwork) {
        this.artwork = artwork;
    }

    public byte[] getImage() {
        return image;
    }

    public void setImage(byte[] image) {
        this.image = image;
    }
}

