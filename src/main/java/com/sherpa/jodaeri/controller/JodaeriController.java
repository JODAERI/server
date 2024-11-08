package com.sherpa.jodaeri.controller;

import com.sherpa.jodaeri.dto.RequestDto;
import com.sherpa.jodaeri.dto.ResponseDto;
import com.sherpa.jodaeri.service.JodaeriService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class JodaeriController {

    private final JodaeriService jodaeriService;

    @PostMapping("/question")
    public ResponseEntity<ResponseDto> search(@RequestBody RequestDto request) {
        ResponseDto response = jodaeriService.answer(request);
        return ResponseEntity.ok()
                .body(response);
    }
}
