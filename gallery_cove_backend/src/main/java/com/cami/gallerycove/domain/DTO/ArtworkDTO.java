package com.cami.gallerycove.domain.DTO;

import lombok.*;

import java.time.LocalDate;

@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
public class ArtworkDTO {

    private Long idArtwork;
    private Long userId;
    private String description;
    private Double price;
    private String categoryName;
    private LocalDate date;
    private String title;

}
