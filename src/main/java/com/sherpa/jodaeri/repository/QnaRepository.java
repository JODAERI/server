package com.sherpa.jodaeri.repository;

import com.sherpa.jodaeri.domain.Qna;
import com.sherpa.jodaeri.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface QnaRepository extends JpaRepository<Qna, Long> {
    List<Qna> findByUser(User user);
}