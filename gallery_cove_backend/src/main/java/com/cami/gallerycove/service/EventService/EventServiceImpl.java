package com.cami.gallerycove.service.EventService;

import com.cami.gallerycove.domain.DTO.EventDTO;
import com.cami.gallerycove.domain.DTOforFE.EventDTOforFE;
import com.cami.gallerycove.domain.exception.EntityNotFoundException;
import com.cami.gallerycove.domain.mapper.EventMapper;
import com.cami.gallerycove.domain.model.Event;
import com.cami.gallerycove.domain.model.JoinEvent;
import com.cami.gallerycove.repository.EventRepository;
import com.cami.gallerycove.repository.JoinEventRepository;
import com.cami.gallerycove.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class EventServiceImpl implements EventService {

    private final EventRepository eventRepository;
    private final EventMapper eventMapper;
    private final UserRepository userRepository;
    private final JoinEventRepository joinEventRepository;

    @Autowired
    public EventServiceImpl(EventRepository eventRepository, EventMapper eventMapper, UserRepository userRepository, JoinEventRepository joinEventRepository) {
        this.eventRepository = eventRepository;
        this.eventMapper = eventMapper;
        this.userRepository = userRepository;
        this.joinEventRepository = joinEventRepository;
    }

    @Override
    public List<EventDTO> getAllEvents() {
        List<Event> events = eventRepository.findAll();
        return events.stream()
                .map(eventMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<EventDTOforFE> getAllEventsForFE() {
        LocalDateTime now = LocalDateTime.now();
        List<Event> events = eventRepository.findByOrderByDateAscTimeAsc();
        return events.stream()
                .filter(event -> LocalDateTime.of(event.getDate(), event.getTime()).isAfter(now))
                .map(eventMapper::toDTOforFE)
                .collect(Collectors.toList());
    }

    @Override
    public List<EventDTOforFE> getAllEventsForFEThatPassed() {
        LocalDateTime now = LocalDateTime.now();
        List<Event> events = eventRepository.findByOrderByDateAscTimeAsc();
        return events.stream()
                .filter(event -> LocalDateTime.of(event.getDate(), event.getTime()).isBefore(now))
                .sorted(Comparator.comparing((Event event) -> LocalDateTime.of(event.getDate(), event.getTime())).reversed())
                .map(eventMapper::toDTOforFE)
                .collect(Collectors.toList());
    }

    @Override
    public EventDTO getEventById(Long id) {
        Optional<Event> optionalEvent = eventRepository.findById(id);
        return optionalEvent.map(eventMapper::toDTO).orElse(null);
    }

    @Override
    public void saveEvent(EventDTO eventDTO) {
        eventRepository.save(eventMapper.toEntity(eventDTO));
    }

    @Override
    public void deleteEvent(Long id) {
        eventRepository.deleteById(id);
    }

    @Override
    public void updateEvent(Long id, EventDTO updatedEventDTO) {
        Optional<Event> existingEventOptional = eventRepository.findById(id);

        if (existingEventOptional.isPresent()) {
            Event existingEvent = existingEventOptional.get();
            Event updatedEvent = eventMapper.toEntity(updatedEventDTO);

            existingEvent.setUser(updatedEvent.getUser());
            existingEvent.setDescriptionMail(updatedEvent.getDescriptionMail());
            existingEvent.setDate(updatedEvent.getDate());
            existingEvent.setLocation(updatedEvent.getLocation());
            existingEvent.setTitle(updatedEvent.getTitle());
            existingEvent.setTime(updatedEvent.getTime());

            eventRepository.save(existingEvent);
        } else {
            throw new EntityNotFoundException("Event with ID " + id + " not found");
        }
    }

    @Override
    public List<EventDTOforFE> getAllEventsThatFollowByUser(Long userId) {
        LocalDateTime now = LocalDateTime.now();
        return eventRepository.findAllByUser(userRepository.findById(userId).orElse(null))
                .stream()
                .filter(event -> LocalDateTime.of(event.getDate(), event.getTime()).isAfter(now))
                .sorted(Comparator.comparing(event -> LocalDateTime.of(event.getDate(), event.getTime())))
                .map(eventMapper::toDTOforFE)
                .collect(Collectors.toList());
    }

    @Override
    public List<EventDTOforFE> getAllEventsThatPassedByUser(Long userId) {
        LocalDateTime now = LocalDateTime.now();

        List<EventDTOforFE> eventsCreated = eventRepository.findAllByUser(userRepository.findById(userId).orElse(null))
                .stream()
                .filter(event -> LocalDateTime.of(event.getDate(), event.getTime()).isBefore(now))
                .map(eventMapper::toDTOforFE).toList();

        List<EventDTOforFE> eventsJoined = joinEventRepository.findByUser(userRepository.findById(userId).orElse(null))
                .stream()
                .map(JoinEvent::getEvent)
                .filter(event -> LocalDateTime.of(event.getDate(), event.getTime()).isBefore(now))
                .map(eventMapper::toDTOforFE).toList();

        List<EventDTOforFE> combinedEvents = new ArrayList<>();
        combinedEvents.addAll(eventsCreated);
        combinedEvents.addAll(eventsJoined);

        combinedEvents.sort(Comparator.comparing((EventDTOforFE event) -> LocalDateTime.of(event.getDate(), event.getTime())).reversed());
        return combinedEvents;
    }
}
