package com.sherpa.jodaeri.dto;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class QnaDto {
    private String question;
    private LocalDateTime questionCreatedAt;
    private String answer;
    private LocalDateTime answerCreatedAt;
}