package com.sherpa.jodaeri.repository;

import com.sherpa.jodaeri.domain.Answer;
import com.sherpa.jodaeri.domain.Question;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AnswerRepository extends JpaRepository<Answer, Long> {
    Optional<Answer> findByQuestion(Question question);
}
