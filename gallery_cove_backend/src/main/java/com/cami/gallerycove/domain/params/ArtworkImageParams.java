package com.cami.gallerycove.domain.params;

import lombok.*;
import org.springframework.web.multipart.MultipartFile;

@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
public class ArtworkImageParams {

    private Long artworkId;
    private MultipartFile photo;
}
