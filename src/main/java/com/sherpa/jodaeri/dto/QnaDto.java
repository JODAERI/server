package com.sherpa.jodaeri.dto;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
@JsonNaming(value = PropertyNamingStrategies.SnakeCaseStrategy.class)
public class QnaDto {
    private String question;
    private LocalDateTime questionCreatedAt;
    private String answer;
    private LocalDateTime answerCreatedAt;
}