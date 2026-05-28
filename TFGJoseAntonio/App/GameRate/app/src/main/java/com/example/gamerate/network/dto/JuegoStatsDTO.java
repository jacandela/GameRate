package com.example.gamerate.network.dto;

import java.io.Serializable;

public class JuegoStatsDTO implements Serializable {
    private Double notaMedia;
    private Long totalResenas;

    public JuegoStatsDTO() {}

    public Double getNotaMedia() { return notaMedia; }
    public void setNotaMedia(Double notaMedia) { this.notaMedia = notaMedia; }

    public Long getTotalResenas() { return totalResenas; }
    public void setTotalResenas(Long totalResenas) { this.totalResenas = totalResenas; }
}