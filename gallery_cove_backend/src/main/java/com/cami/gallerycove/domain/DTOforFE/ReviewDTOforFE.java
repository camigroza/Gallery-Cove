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
public class ReviewDTOforFE {

    private Long idReview;
    private Integer numberOfStars;
    private String description;
    private String userName;
    private Long artworkId;
    private LocalDate date;
    private Long userId;
    private LocalTime time;
    private byte[] userPhoto;
}
