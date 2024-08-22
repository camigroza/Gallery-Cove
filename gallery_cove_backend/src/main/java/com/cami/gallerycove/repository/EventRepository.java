package com.cami.gallerycove.repository;

import com.cami.gallerycove.domain.model.Event;
import com.cami.gallerycove.domain.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EventRepository extends JpaRepository<Event, Long> {

    List<Event> findByOrderByDateAscTimeAsc();

    List<Event> findAllByUser(User user);

}
