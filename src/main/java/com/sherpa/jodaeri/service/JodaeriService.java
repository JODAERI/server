package com.sherpa.jodaeri.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sherpa.jodaeri.domain.Answer;
import com.sherpa.jodaeri.domain.Question;
import com.sherpa.jodaeri.domain.User;
import com.sherpa.jodaeri.dto.*;
import com.sherpa.jodaeri.repository.AnswerRepository;
import com.sherpa.jodaeri.repository.QuestionRepository;
import com.sherpa.jodaeri.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class JodaeriService {
    private final UserRepository userRepository;
    private final QuestionRepository questionRepository;
    private final AnswerRepository answerRepository;

    private final ChatClient chatClient;
    private final ObjectMapper objectMapper;

    private final String LEARN_PROMPTS = loadPrompts();

    private static String loadPrompts() {
        try {
            Path filePath = Paths.get("src/main/resources/intent.txt");
            return Files.readString(filePath);
        } catch (IOException e) {
            throw new IllegalStateException("intent.txt 불러오기 실패", e);
        }
    }

    public AnswerResponse answerWithRag(QuestionRequest request) {
        User user = getUser(request);
        log.info("RAG 질문의 user: {}", user.getId());
        String ragPrompt = createPrompt(user, request) + "\n인터넷 검색을 기반으로 답변을 생성하라";
        String response = generateResponse(ragPrompt);
        String finalResponse = request.getIsShort()
                ? generateShortResponse(response)
                : response;
        saveQna(user, request.getQuestion(), finalResponse);

        return buildResponseDto(user, finalResponse);
    }

    private String createPrompt(User user, QuestionRequest request) {
        String basePrompt = request.getIsFirst() ? LEARN_PROMPTS : LEARN_PROMPTS + "지금까지의 질문 기록:\n" + getQnaHistory(user);
        return basePrompt + "사용자의 질문은 다음과 같다.\nquestion:\s" + request.getQuestion();
    }

    public AnswerResponse answer(QuestionRequest request) {
        User user = getUser(request);
        log.info("질문의 user: {}", user.getId());
        String prompt = createPrompt(user, request);
        String response = generateResponse(prompt);
        String finalResponse = request.getIsShort()
                ? generateShortResponse(response)
                : response;
        saveQna(user, request.getQuestion(), finalResponse);

        return buildResponseDto(user, finalResponse);
    }

    private User getUser(QuestionRequest request) {
        if (request.getIsFirst()) {
            User user = User.builder().build();
            userRepository.save(user);
            return user;
        } else {
            return userRepository.findById(request.getUserId())
                    .orElseThrow(() -> new RuntimeException("사용자 없음"));
        }
    }

    private String generateResponse(String prompt) {
        return chatClient.prompt()
                .user(userSpec -> userSpec.text(prompt))
                .call()
                .content();
    }

    private String generateShortResponse(String answer) {
        String prompt = "다음 문장을 공백을 포함하여 150자 이내로 요약하라.:\n" + answer;

        return chatClient.prompt()
                .user(userSpec -> userSpec.text(prompt))
                .call()
                .content()
                .strip();
    }


    private String getQnaHistory(User user) {
        List<Question> questions = questionRepository.findByUser(user);
        List<QnaDto> qnaHistory = questions.stream()
                .map(question -> {
                    Answer answer = answerRepository.findByQuestion(question)
                            .orElseThrow(RuntimeException::new);
                    return QnaDto.builder()
                            .question(question.getQuestion())
                            .questionCreatedAt(question.getCreatedAt())
                            .answer(answer != null ? answer.getAnswer() : "Answer 없음")
                            .answerCreatedAt(answer != null ? answer.getCreatedAt() : null)
                            .build();
                })
                .toList();

        return qnaHistory.stream()
                .map(qna -> String.format("Q: %s (질문 시각: %s)\nA: %s (답변 시각: %s)\n",
                        qna.getQuestion(),
                        qna.getQuestionCreatedAt(),
                        qna.getAnswer(),
                        qna.getAnswerCreatedAt() != null ? qna.getAnswerCreatedAt() : "답변 없음"))
                .collect(Collectors.joining("\n"));
    }

    private void saveQna(User user, String question, String answer) {
        Question q = Question.builder()
                .user(user)
                .question(question)
                .build();
        questionRepository.save(q);

        Answer a = Answer.builder()
                .question(q)
                .answer(answer)
                .build();
        answerRepository.save(a);

        log.info("QNA 저장 > > user: {}, question: {}, answer: {}", user.getId(), q.getId(), a.getId());
    }

    private AnswerResponse buildResponseDto(User user, String response) {
        return AnswerResponse.builder()
                .userId(user != null ? user.getId() : null)
                .answer(response)
                .build();
    }

    public QnasResponse findQnas(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("사용자 없음 id: " + userId));

        List<QnaDto> qnas = questionRepository.findByUser(user).stream()
                .map(question -> {
                    Answer answer = answerRepository.findByQuestion(question)
                            .orElseThrow(RuntimeException::new);
                    return QnaDto.builder()
                            .question(question.getQuestion())
                            .questionCreatedAt(question.getCreatedAt())
                            .answer(answer != null ? answer.getAnswer() : "answer 없음")
                            .answerCreatedAt(answer != null ? answer.getCreatedAt() : null)
                            .build();
                })
                .collect(Collectors.toList());

        return QnasResponse.builder()
                .userId(userId)
                .qnas(qnas)
                .build();
    }

    public QuickQuestionResponse findQuickQuestion(String category) {
        log.info("요청 카테고리: {}", category);
        try {
            Map<String, List<String>> quickQuestions = objectMapper.readValue(
                    Paths.get("src/main/resources/quick.json").toFile(),
                    new TypeReference<Map<String, List<String>>>() {}
            );
            List<String> questions = quickQuestions.getOrDefault(category, List.of());

            return QuickQuestionResponse.builder()
                    .questions(questions)
                    .time(LocalDateTime.now())
                    .build();

        } catch (IOException e) {
            log.error("quick.json 파일을 읽는 중 오류 발생: ", e);
            return QuickQuestionResponse.builder()
                    .questions(List.of())
                    .build();
        }
    }
}