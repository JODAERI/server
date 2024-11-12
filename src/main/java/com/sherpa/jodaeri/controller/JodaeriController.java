package com.sherpa.jodaeri.controller;

import com.sherpa.jodaeri.dto.*;
import com.sherpa.jodaeri.service.JodaeriService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class JodaeriController {

    private final JodaeriService jodaeriService;

    @PostMapping("/question")
    public ResponseEntity<AnswerResponse> postQuestion(@RequestBody QuestionRequest request) {
        AnswerResponse response = jodaeriService.answer(request);
        return ResponseEntity.ok()
                .body(response);
    }

    @GetMapping("/qna/{userId}")
    public ResponseEntity<QnasResponse> getQnas(@PathVariable Long userId) {
        QnasResponse response = jodaeriService.findQnas(userId);
        return ResponseEntity.ok()
                .body(response);
    }

    @GetMapping("/question/quick")
    public ResponseEntity<QuickQuestionResponse> getQuickQuestion(@RequestBody QuickQuestionRequest request) {
        QuickQuestionResponse response = jodaeriService.findQuickQuestion(request.getCategory());
        return ResponseEntity.ok()
                .body(response);
    }
}
