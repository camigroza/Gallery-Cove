package com.cami.gallerycove.domain.model;

import jakarta.persistence.*;

import java.time.LocalDate;
import java.time.LocalTime;

@Entity
@Table(name = "events")
public class Event {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_event")
    private Long idEvent;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @Column(name = "description_mail")
    private String descriptionMail;

    @Column(name = "date")
    private LocalDate date;

    @Column(name = "location")
    private String location;

    @Column(name = "title")
    private String title;

    @Column(name = "time")
    private LocalTime time;

    //constructors, getters, setters

    public Event() {
    }

    public Event(Long idEvent, User user, String descriptionMail, LocalDate date, String location, String title, LocalTime time) {
        this.idEvent = idEvent;
        this.user = user;
        this.descriptionMail = descriptionMail;
        this.date = date;
        this.location = location;
        this.title = title;
        this.time = time;
    }

    public Long getIdEvent() {
        return idEvent;
    }

    public void setIdEvent(Long idEvent) {
        this.idEvent = idEvent;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getDescriptionMail() {
        return descriptionMail;
    }

    public void setDescriptionMail(String descriptionMail) {
        this.descriptionMail = descriptionMail;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public LocalTime getTime() {
        return time;
    }

    public void setTime(LocalTime time) {
        this.time = time;
    }
}

