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

    // 질문
    @PostMapping("/question")
    public ResponseEntity<AnswerResponse> postQuestion(@RequestBody QuestionRequest request) {
        log.info("Request to POST question");
        AnswerResponse response = jodaeriService.answer(request);
        log.info("Response to POST question");
        return ResponseEntity.ok()
                .body(response);
    }

    // QNA 조회
    @GetMapping("/qna/{userId}")
    public ResponseEntity<QnasResponse> getQnas(@PathVariable Long userId) {
        log.info("Request to GET qnas");
        QnasResponse response = jodaeriService.findQnas(userId);
        log.info("Response to GET qnas");
        return ResponseEntity.ok()
                .body(response);
    }

    // 빠른 질문 찾기
    @PostMapping("/question/quick")
    public ResponseEntity<QuickQuestionResponse> getQuickQuestion(@RequestBody QuickQuestionRequest request) {
        log.info("Request to POST quick question");
        QuickQuestionResponse response = jodaeriService.findQuickQuestion(request.getCategory());
        log.info("Response to POST quick question");
        return ResponseEntity.ok()
                .body(response);
    }
}
