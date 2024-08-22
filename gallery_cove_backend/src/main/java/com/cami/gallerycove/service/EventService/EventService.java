package com.cami.gallerycove.service.EventService;

import com.cami.gallerycove.domain.DTO.ArtworkDTO;
import com.cami.gallerycove.domain.DTO.EventDTO;
import com.cami.gallerycove.domain.DTOforFE.EventDTOforFE;

import java.util.List;

public interface EventService {

    List<EventDTO> getAllEvents();

    List<EventDTOforFE> getAllEventsForFE();

    List<EventDTOforFE> getAllEventsForFEThatPassed();

    EventDTO getEventById(Long id);

    void saveEvent(EventDTO eventDTO);

    void deleteEvent(Long id);

    void updateEvent(Long id, EventDTO updatedEventDTO);

    List<EventDTOforFE> getAllEventsThatFollowByUser(Long userId);

    List<EventDTOforFE> getAllEventsThatPassedByUser(Long userId);
}

