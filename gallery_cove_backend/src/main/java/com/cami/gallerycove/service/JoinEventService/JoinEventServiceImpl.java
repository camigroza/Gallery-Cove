package com.cami.gallerycove.service.JoinEventService;

import com.cami.gallerycove.domain.DTO.JoinEventDTO;
import com.cami.gallerycove.domain.DTOforFE.EventDTOforFE;
import com.cami.gallerycove.domain.mapper.EventMapper;
import com.cami.gallerycove.domain.mapper.JoinEventMapper;
import com.cami.gallerycove.domain.model.JoinEvent;
import com.cami.gallerycove.domain.model.JoinEventId;
import com.cami.gallerycove.domain.model.User;
import com.cami.gallerycove.repository.EventRepository;
import com.cami.gallerycove.repository.JoinEventRepository;
import com.cami.gallerycove.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class JoinEventServiceImpl implements JoinEventService {

    private final JoinEventRepository joinEventRepository;
    private final EventRepository eventRepository;
    private final JoinEventMapper joinEventMapper;
    private final UserRepository userRepository;
    private final EventMapper eventMapper;

    @Autowired
    public JoinEventServiceImpl(JoinEventRepository joinEventRepository, EventRepository eventRepository, JoinEventMapper joinEventMapper, UserRepository userRepository, EventMapper eventMapper) {
        this.joinEventRepository = joinEventRepository;
        this.eventRepository = eventRepository;
        this.joinEventMapper = joinEventMapper;
        this.userRepository = userRepository;
        this.eventMapper = eventMapper;
    }

    @Override
    public void saveJoinEvent(JoinEventDTO joinEventDTO) {
        joinEventRepository.save(joinEventMapper.toEntity(joinEventDTO));
    }

    @Override
    public void deleteJoinEvent(JoinEventDTO joinEventDTO) {
        JoinEventId id = new JoinEventId(joinEventMapper.toEntity(joinEventDTO));
        joinEventRepository.deleteById(id);
    }

    @Override
    public boolean isUserJoined(JoinEventDTO joinEventDTO) {
        JoinEventId id = new JoinEventId(joinEventMapper.toEntity(joinEventDTO));
        Optional<JoinEvent> optionalJoinEvent = joinEventRepository.findById(id);
        return optionalJoinEvent.isPresent();
    }

    @Override
    public List<User> getAllUsersByEvent(Long eventId) {
        return joinEventRepository.findByEvent(eventRepository.findById(eventId).orElse(null))
                .stream()
                .map(JoinEvent::getUser)
                .collect(Collectors.toList());
    }

    @Override
    public List<EventDTOforFE> getAllEventsByUser(Long userId) {
        LocalDateTime now = LocalDateTime.now();
        return joinEventRepository.findByUser(userRepository.findById(userId).orElse(null))
                .stream()
                .map(JoinEvent::getEvent)
                .filter(event -> LocalDateTime.of(event.getDate(), event.getTime()).isAfter(now))
                .sorted(Comparator.comparing(event -> LocalDateTime.of(event.getDate(), event.getTime())))
                .map(eventMapper::toDTOforFE)
                .collect(Collectors.toList());
    }
}
