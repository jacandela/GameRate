package com.gamerate.gamerate.dto.entity.Juego;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class JuegoStatsDTO {

    private Double notaMedia;
    private Long totalResenas;

    public JuegoStatsDTO(Double notaMedia, Long totalResenas) {
        this.notaMedia = (notaMedia != null) ? Math.round(notaMedia * 10.0) / 10.0 : 0.0;
        this.totalResenas = (totalResenas != null) ? totalResenas : 0L;
    }
}