package com.sherpa.jodaeri.repository;

import com.sherpa.jodaeri.domain.Question;
import com.sherpa.jodaeri.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface QuestionRepository extends JpaRepository<Question, Long> {
    List<Question> findByUser(User user);
}