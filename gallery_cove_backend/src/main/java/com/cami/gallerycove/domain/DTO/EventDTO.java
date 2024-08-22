package com.cami.gallerycove.domain.DTO;

import lombok.*;

import java.time.LocalDate;
import java.time.LocalTime;

@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
public class EventDTO {

    private Long idEvent;
    private Long userId;
    private String descriptionMail;
    private LocalDate date;
    private String location;
    private String title;
    private LocalTime time;
}
