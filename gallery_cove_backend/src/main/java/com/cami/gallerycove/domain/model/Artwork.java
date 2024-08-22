package com.cami.gallerycove.domain.model;

import jakarta.persistence.*;

import java.time.LocalDate;

@Entity
@Table(name = "artworks")
public class Artwork {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_artwork")
    private Long idArtwork;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @Column(name = "description")
    private String description;

    @Column(name = "price")
    private Double price;

    @ManyToOne
    @JoinColumn(name = "category_id")
    private Category category;

    @Column(name = "date")
    private LocalDate date;

    @Column(name = "title")
    private String title;

    //constructors, getters, setters

    public Artwork() {
    }

    public Artwork(User user, String description, Double price, Category category, String title) {
        this.user = user;
        this.description = description;
        this.price = price;
        this.category = category;
        this.date = LocalDate.now();
        this.title = title;
    }

    public Long getIdArtwork() {
        return idArtwork;
    }

    public void setIdArtwork(Long idArtwork) {
        this.idArtwork = idArtwork;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}

