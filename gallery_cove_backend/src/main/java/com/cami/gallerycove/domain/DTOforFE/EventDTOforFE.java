package com.cami.gallerycove.domain.DTOforFE;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalTime;

@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
public class EventDTOforFE {

    private Long idEvent;
    private String userName;
    private String descriptionMail;
    private LocalDate date;
    private String location;
    private String title;
    private LocalTime time;
    private Long userId;
    private Integer numberOfUsersJoined;
}
