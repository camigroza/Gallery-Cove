package com.cami.gallerycove.repository;

import com.cami.gallerycove.domain.model.*;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface JoinEventRepository extends JpaRepository<JoinEvent, JoinEventId> {
    List<JoinEvent> findByEvent(Event event);

    List<JoinEvent> findByUser(User user);
}