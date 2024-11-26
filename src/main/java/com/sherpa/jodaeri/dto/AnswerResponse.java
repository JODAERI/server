package com.sherpa.jodaeri.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class AnswerResponse {
    private Long userId;
    private String answer;
    private String shortAnswer;
}