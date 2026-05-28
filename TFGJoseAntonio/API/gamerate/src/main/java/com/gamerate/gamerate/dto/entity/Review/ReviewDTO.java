package com.gamerate.gamerate.dto.entity.Review;

import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class ReviewDTO {
    private Long id;
    private Long userId;
    private Long gameId;
    private BigDecimal score;
    private String username;
    private String comment;
    private LocalDateTime createdAt;
}