package com.cami.gallerycove.domain.DTO;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
public class FavoriteDTO {

    private Long userId;
    private Long artworkId;
}
