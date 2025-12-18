package com.hackerrank.sample.dto;

import java.time.LocalDateTime;
import lombok.Data;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class QuestionDTO {
    private Long id;
    private String question;
    private String answer;
    private String userName;
    private LocalDateTime createdAt;
    private LocalDateTime answeredAt;
}
