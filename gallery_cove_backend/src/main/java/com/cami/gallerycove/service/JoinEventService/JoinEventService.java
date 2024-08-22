package com.cami.gallerycove.service.JoinEventService;

import com.cami.gallerycove.domain.DTO.JoinEventDTO;
import com.cami.gallerycove.domain.DTOforFE.EventDTOforFE;
import com.cami.gallerycove.domain.model.Event;
import com.cami.gallerycove.domain.model.User;

import java.util.List;

public interface JoinEventService {

    void saveJoinEvent(JoinEventDTO joinEventDTO);

    void deleteJoinEvent(JoinEventDTO joinEventDTO);

    boolean isUserJoined(JoinEventDTO joinEventDTO);

    List<User> getAllUsersByEvent(Long eventId);

    List<EventDTOforFE> getAllEventsByUser(Long userId);
}
