package com.cami.gallerycove.domain.model;

import jakarta.persistence.*;

@Entity
@Table(name = "join_events")
@IdClass(JoinEventId.class)
public class JoinEvent {

    @Id
    @ManyToOne
    @JoinColumn(name = "event_id")
    private Event event;

    @Id
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    //constructors, getters, setters

    public JoinEvent() {
    }

    public JoinEvent(Event event, User user) {
        this.event = event;
        this.user = user;
    }

    public Event getEvent() {
        return event;
    }

    public void setEvent(Event event) {
        this.event = event;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
