package com.cami.gallerycove.domain.DTO;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
public class ArtworkImageDTO {

    private Long idArtworkImage;
    private Long artworkId;
    private byte[] image;
}
