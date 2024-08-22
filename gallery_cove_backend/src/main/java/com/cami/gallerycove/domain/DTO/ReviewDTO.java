package com.cami.gallerycove.domain.DTO;

import lombok.*;

import java.time.LocalDate;
import java.time.LocalTime;

@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
public class ReviewDTO {

    private Long idReview;
    private Integer numberOfStars;
    private String description;
    private Long userId;
    private Long artworkId;
    private LocalDate date;
    private LocalTime time;
}
