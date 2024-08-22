package com.cami.gallerycove.controller;

import com.cami.gallerycove.domain.DTO.EventDTO;
import com.cami.gallerycove.domain.DTO.JoinEventDTO;
import com.cami.gallerycove.domain.DTOforFE.EventDTOforFE;
import com.cami.gallerycove.domain.exception.EntityNotFoundException;
import com.cami.gallerycove.domain.mapper.EventMapper;
import com.cami.gallerycove.domain.model.Event;
import com.cami.gallerycove.domain.model.JoinEvent;
import com.cami.gallerycove.domain.model.MailStructure;
import com.cami.gallerycove.domain.model.User;
import com.cami.gallerycove.service.EventService.EventService;
import com.cami.gallerycove.service.JoinEventService.JoinEventService;
import com.cami.gallerycove.service.MailService.MailService;
import com.cami.gallerycove.service.UserService.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@CrossOrigin(origins = "https://gallery-cove.onrender.com", allowedHeaders = "*")
@RequestMapping("/event")
public class EventController {

    private final EventService eventService;
    private final JoinEventService joinEventService;
    private final MailService mailService;
    private final EventMapper eventMapper;
    private final UserService userService;

    @Autowired
    public EventController(EventService eventService, JoinEventService joinEventService, MailService mailService, EventMapper eventMapper, UserService userService) {
        this.eventService = eventService;
        this.joinEventService = joinEventService;
        this.mailService = mailService;
        this.eventMapper = eventMapper;
        this.userService = userService;
    }

    @GetMapping("/getAll")
    public ResponseEntity<List<EventDTO>> getAllEvents() {
        try {
            List<EventDTO> events = eventService.getAllEvents();
            return ResponseEntity.ok().body(events);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @GetMapping("/get/{id}")
    public ResponseEntity<EventDTO> getEventById(@PathVariable Long id) {
        try {
            EventDTO event = eventService.getEventById(id);
            if (event != null) {
                return ResponseEntity.ok().body(event);
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @PostMapping("/add")
    public ResponseEntity<String> addEvent(@RequestBody EventDTO eventDTO) {
        try {
            eventService.saveEvent(eventDTO);

            Event event = eventMapper.toEntity(eventDTO);
            String title = "Eveniment adaugat: " + event.getTitle();
            String description = "Dragi iubitori de arta,\n\n" +
                    "Va informam cu mare drag ca un nou eveniment a fost adaugat in calendarul nostru!\n\n" +
                    "Evenimentul intitulat \"" + event.getTitle() + "\" este programat pentru data de " +
                    event.getDate() + ", incepand cu ora " + event.getTime() + ". " +
                    "Locatia: " + event.getLocation() + ".\n\n" +
                    "Gazda noastra, " + event.getUser().getName() + ", va transmite urmatoarele:\n\n" +
                    "\"" + event.getDescriptionMail() + "\"\n\nCu stima,\nEchipa Gallery Cove";

//            MailStructure mailStructure = new MailStructure(title, description);
//            List<String> emails = userService.getAllUsersEmail();
//            for (String email: emails) {
//                mailService.sendEmail(email, mailStructure);
//            }

            return ResponseEntity.status(HttpStatus.CREATED).body("Event saved successfully!");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("ERROR:\n" + e.getMessage());
        }
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<String> updateEvent(@PathVariable Long id, @RequestBody EventDTO eventDTO) {
        try {
            eventService.updateEvent(id, eventDTO);

            Event event = eventMapper.toEntity(eventDTO);
            String title = "Eveniment actualizat: " + event.getTitle();
            String description = "Dragi iubitori de arta,\n\n" +
                    "Va informam ca detalii privind evenimentul intitulat \"" + event.getTitle() +
                    "\" au fost actualizate!\n\n" +
                    "Locatia: " + event.getLocation() +
                    "\nData: " + event.getDate() +
                    "\nOra: " + event.getTime() +
                    "\n\nCu stima,\nEchipa Gallery Cove";

//            MailStructure mailStructure = new MailStructure(title, description);
//            List<String> emails = joinEventService.getAllUsersByEvent(id)
//                    .stream()
//                    .map(User::getEmail).toList();
//            for (String email: emails) {
//                mailService.sendEmail(email, mailStructure);
//            }

            return ResponseEntity.ok().body("Event updated successfully!");
        } catch (EntityNotFoundException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("ERROR:\n" + ex.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("ERROR:\n" + e.getMessage());
        }
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> deleteEvent(@PathVariable Long id) {
        try {
            EventDTO eventDTO = eventService.getEventById(id);

            if (eventDTO != null) {
//                List<String> emails = joinEventService.getAllUsersByEvent(id)
//                        .stream()
//                        .map(User::getEmail).toList();

                List<String> emails = userService.getAllUsersEmail();

                Event event = eventMapper.toEntity(eventDTO);

                String title = "Eveniment anulat: " + event.getTitle();
                String description = "Dragi iubitori de arta,\n\n" +
                        "Va informam cu regret ca evenimentul intitulat \"" + event.getTitle() +
                        "\", programat pentru data de " + event.getDate() + ", ora " +
                        event.getTime() + ", a fost anulat.\n\n" +
                        "\n\nCu stima,\nEchipa Gallery Cove";

//                MailStructure mailStructure = new MailStructure(title, description);
//                for (String email: emails) {
//                    mailService.sendEmail(email, mailStructure);
//                }

                eventService.deleteEvent(id);
            }

            return ResponseEntity.ok().body("Event deleted successfully!");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("ERROR:\n" + e.getMessage());
        }
    }

    @PostMapping("/join")
    public ResponseEntity<String> joinEvent(@RequestBody JoinEventDTO joinEventDTO) {
        try {
            joinEventService.saveJoinEvent(joinEventDTO);
            return ResponseEntity.status(HttpStatus.CREATED).body("User joined event successfully!");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("ERROR:\n" + e.getMessage());
        }
    }

    @DeleteMapping("/leave")
    public ResponseEntity<String> leaveEvent(@RequestBody JoinEventDTO joinEventDTO) {
        try {
            joinEventService.deleteJoinEvent(joinEventDTO);
            return ResponseEntity.ok().body("User left event successfully!");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("ERROR:\n" + e.getMessage());
        }
    }

    @GetMapping("/isUserJoined")
    public ResponseEntity<Boolean> isUserJoined(@RequestBody JoinEventDTO joinEventDTO) {
        try {
            boolean isJoined = joinEventService.isUserJoined(joinEventDTO);
            return ResponseEntity.ok().body(isJoined);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @GetMapping("/whereJoined/{userId}")
    public ResponseEntity<List<EventDTOforFE>> getEventsIdByUserId(@PathVariable Long userId) {
        try {
            List<EventDTOforFE> events = joinEventService.getAllEventsByUser(userId);
            return ResponseEntity.ok().body(events);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @GetMapping("/users/{eventId}")
    public ResponseEntity<List<User>> getUsersByEventId(@PathVariable Long eventId) {
        try {
            List<User> users = joinEventService.getAllUsersByEvent(eventId);
            return ResponseEntity.ok().body(users);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @GetMapping("/DTOforFE")
    public ResponseEntity<List<EventDTOforFE>> getAllEventsForFE() {
        try {
            List<EventDTOforFE> events = eventService.getAllEventsForFE();
            return ResponseEntity.ok().body(events);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @GetMapping("/DTOforFEPast")
    public ResponseEntity<List<EventDTOforFE>> getAllEventsForFEPast() {
        try {
            List<EventDTOforFE> events = eventService.getAllEventsForFEThatPassed();
            return ResponseEntity.ok().body(events);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @GetMapping("/getAllThatFollowByUser/{userId}")
    public ResponseEntity<List<EventDTOforFE>> getAllEventsThatFollowByUser(@PathVariable Long userId) {
        try {
            List<EventDTOforFE> events = eventService.getAllEventsThatFollowByUser(userId);
            return ResponseEntity.ok().body(events);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @GetMapping("/getAllThatPassedByUser/{userId}")
    public ResponseEntity<List<EventDTOforFE>> getAllEventsThatPassedByUser(@PathVariable Long userId) {
        try {
            List<EventDTOforFE> events = eventService.getAllEventsThatPassedByUser(userId);
            return ResponseEntity.ok().body(events);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }
}
