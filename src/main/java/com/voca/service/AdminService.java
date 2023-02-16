package com.voca.service;

import com.voca.entity.Quiz;
import com.voca.repository.QuizRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class AdminService {
	
	@Autowired
    private final QuizRepository quizRepository;

    public Quiz saveVoca(Quiz quiz) {
    	return quizRepository.save(quiz);
    }

}
