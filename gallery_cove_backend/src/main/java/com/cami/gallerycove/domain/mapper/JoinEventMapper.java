package com.cami.gallerycove.domain.mapper;

import com.cami.gallerycove.domain.DTO.JoinEventDTO;
import com.cami.gallerycove.domain.model.JoinEvent;
import com.cami.gallerycove.repository.EventRepository;
import com.cami.gallerycove.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class JoinEventMapper {

    private final EventRepository eventRepository;
    private final UserRepository userRepository;

    @Autowired
    public JoinEventMapper(EventRepository eventRepository, UserRepository userRepository) {
        this.eventRepository = eventRepository;
        this.userRepository = userRepository;
    }

    public JoinEventDTO toDTO(JoinEvent joinEvent) {
        JoinEventDTO joinEventDTO = new JoinEventDTO();

        joinEventDTO.setEventId(joinEvent.getEvent().getIdEvent());
        joinEventDTO.setUserId(joinEvent.getUser().getIdUser());

        return joinEventDTO;
    }

    public JoinEvent toEntity(JoinEventDTO joinEventDTO) {
        JoinEvent joinEvent = new JoinEvent();

        joinEvent.setEvent(eventRepository.findById(joinEventDTO.getEventId()).orElse(null));
        joinEvent.setUser(userRepository.findById(joinEventDTO.getUserId()).orElse(null));

        return joinEvent;
    }
}
