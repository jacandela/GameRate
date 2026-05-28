package com.example.gamerate.network.dto;

import java.io.Serializable;
import java.math.BigDecimal;

public class ReviewDTO implements Serializable {
    private Long id;
    private Long gameId;
    private BigDecimal score;
    private String comment;
    private String username;

    private String createdAt;

    public ReviewDTO() {}

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Long getGameId() { return gameId; }
    public void setGameId(Long gameId) { this.gameId = gameId; }

    public String getComment() { return comment; }
    public void setComment(String comment) { this.comment = comment; }

    public BigDecimal getScore() { return score; }
    public void setScore(BigDecimal score) { this.score = score; }

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public String getCreatedAt() { return createdAt; }
    public void setCreatedAt(String createdAt) { this.createdAt = createdAt; }
}