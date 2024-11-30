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
        AnswerResponse response = jodaeriService.answerWithRag(request);
        return ResponseEntity.ok()
                .body(response);
    }

    // QNA 조회
    @GetMapping("/qna/{userId}")
    public ResponseEntity<QnasResponse> getQnas(@PathVariable Long userId) {
        log.info("Request to GET qnas");
        QnasResponse response = jodaeriService.findQnas(userId);
        return ResponseEntity.ok()
                .body(response);
    }

    // 빠른 질문 찾기
    @PostMapping("/question/quick")
    public ResponseEntity<QuickQuestionResponse> getQuickQuestion(@RequestBody QuickQuestionRequest request) {
        log.info("Request to POST quick question");
        QuickQuestionResponse response = jodaeriService.findQuickQuestion(request.getCategory());
        return ResponseEntity.ok()
                .body(response);
    }
}
