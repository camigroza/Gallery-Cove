package com.cami.gallerycove.domain.mapper;

import com.cami.gallerycove.domain.DTO.EventDTO;
import com.cami.gallerycove.domain.DTOforFE.EventDTOforFE;
import com.cami.gallerycove.domain.model.Event;
import com.cami.gallerycove.domain.model.JoinEvent;
import com.cami.gallerycove.repository.EventRepository;
import com.cami.gallerycove.repository.JoinEventRepository;
import com.cami.gallerycove.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class EventMapper {

    private final UserRepository userRepository;
    private final EventRepository eventRepository;
    private final JoinEventRepository joinEventRepository;

    @Autowired
    public EventMapper(UserRepository userRepository, EventRepository eventRepository, JoinEventRepository joinEventRepository) {
        this.userRepository = userRepository;
        this.eventRepository = eventRepository;
        this.joinEventRepository = joinEventRepository;
    }

    public EventDTO toDTO(Event event) {
        EventDTO eventDTO = new EventDTO();

        eventDTO.setIdEvent(event.getIdEvent());
        eventDTO.setUserId(event.getUser().getIdUser());
        eventDTO.setDescriptionMail(event.getDescriptionMail());
        eventDTO.setDate(event.getDate());
        eventDTO.setLocation(event.getLocation());
        eventDTO.setTitle(event.getTitle());
        eventDTO.setTime(event.getTime());

        return eventDTO;
    }

    public Event toEntity(EventDTO eventDTO) {
        Event event = new Event();

        event.setIdEvent(eventDTO.getIdEvent());
        event.setUser(userRepository.findById(eventDTO.getUserId()).orElse(null));
        event.setDescriptionMail(eventDTO.getDescriptionMail());
        event.setDate(eventDTO.getDate());
        event.setLocation(eventDTO.getLocation());
        event.setTitle(eventDTO.getTitle());
        event.setTime(eventDTO.getTime());

        return event;
    }

    public EventDTOforFE toDTOforFE(Event event) {
        EventDTOforFE eventDTOforFE = new EventDTOforFE();

        eventDTOforFE.setIdEvent(event.getIdEvent());
        eventDTOforFE.setUserName(event.getUser().getName());
        eventDTOforFE.setDescriptionMail(event.getDescriptionMail());
        eventDTOforFE.setDate(event.getDate());
        eventDTOforFE.setLocation(event.getLocation());
        eventDTOforFE.setTitle(event.getTitle());
        eventDTOforFE.setTime(event.getTime());
        eventDTOforFE.setUserId(event.getUser().getIdUser());
        eventDTOforFE.setNumberOfUsersJoined(this.howManyUsersByEvent(event.getIdEvent()));

        return eventDTOforFE;
    }

    public Integer howManyUsersByEvent(Long eventId) {
        return joinEventRepository.findByEvent(eventRepository.findById(eventId).orElse(null))
                .stream()
                .map(JoinEvent::getUser).toList()
                .size();
    }

}
