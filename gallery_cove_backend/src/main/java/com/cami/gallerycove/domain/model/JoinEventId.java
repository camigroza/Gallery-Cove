package com.cami.gallerycove.domain.model;

import java.io.Serializable;
import java.util.Objects;

public class JoinEventId  implements Serializable {

    private Event event;
    private User user;

    //constructors, getters, setters


    public JoinEventId() {
    }

    public JoinEventId(JoinEvent joinEvent) {
        this.event = joinEvent.getEvent();
        this.user = joinEvent.getUser();
    }

    public JoinEventId(Event event, User user) {
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

    //equals, hashcode

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        JoinEventId that = (JoinEventId) o;
        return event.equals(that.event) && user.equals(that.user);
    }

    @Override
    public int hashCode() {
        return Objects.hash(event, user);
    }
}
