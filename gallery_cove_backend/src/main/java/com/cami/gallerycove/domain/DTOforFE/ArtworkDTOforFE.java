package com.cami.gallerycove.domain.DTOforFE;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
public class ArtworkDTOforFE {

    private Long idArtwork;
    private Long idUser;
    private String userName;
    private String description;
    private String title;
    private Double price;
    private String categoryName;
    private LocalDate date;
    private List<byte[]> photos;
}
