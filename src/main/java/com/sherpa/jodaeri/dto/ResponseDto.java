package com.sherpa.jodaeri.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ResponseDto {
    private Long userId;
    private String answer;
}