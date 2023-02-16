package com.voca.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.voca.entity.Quiz;
import com.voca.entity.QuizId;
import com.voca.entity.Wrong;
import com.voca.repository.QuizRepository;
import com.voca.repository.WrongRepository;

import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
public class StudyService {

	@Autowired
	private final QuizRepository quizRepository;
	@Autowired
	private final WrongRepository wrongRepository;

	public Quiz findbyQuizId(int day, int q) {
		QuizId quizId = new QuizId(day, q);
		return quizRepository.findById(quizId).get();
	}

	public List<String> Example(String answer, String category) {
		List<String> ex = new ArrayList<>();
		List<String> ans = quizRepository.findEx(category);
		Collections.shuffle(ans);

		for (int i = 0; i <= ans.size(); i++) {
			if (ans.get(i).contains(answer.substring(0, 2)) == true) {
				continue;
			} else {
				ex.add(ans.get(i));
			}
			if (ex.size() == 3) {
				break;
			}
		}

		ex.add(answer);
		Collections.shuffle(ex);

		return ex;
	}

	public String correctAns(String answer, String ex) {
		if (answer.equals(ex)) {
			return "o";
		} else {
			return "x";
		}
	}

	public void saveWrong(String id, int day, int q) {

		Quiz quiz = quizRepository.findById(new QuizId(day, q)).get();
		Wrong wrong = new Wrong();
		wrong.setMemberId(id);
		wrong.setDay(day);
		wrong.setQ(q);
		wrong.setQuiz(quiz);

		if (wrongRepository.findByMemberIdAndQuiz_DayAndQuiz_Q(id, day, q) == null) {
			wrongRepository.save(wrong);
		}
	}

	public void deleteWrong(String id, int day, int q) {
		wrongRepository.deleteByMemberIdAndQuiz_DayAndQuiz_Q(id, day, q);
	}

	public List<Quiz> printWrong(String id, int day, int page) {
		int batch = 10;
		List<Quiz> quiz = new ArrayList<Quiz>();

		for (int i = 0; i < wrongRepository.findByMemberIdAndQuiz_Day(id, day, PageRequest.of(page - 1, batch))
				.size(); i++) {
			quiz.add(wrongRepository.findByMemberIdAndQuiz_Day(id, day, PageRequest.of(page - 1, batch)).get(i)
					.getQuiz());
		}

		return quiz;
	}

	public Long countWrong(String id, int day) {
		return wrongRepository.countByMemberIdAndQuiz_Day(id, day);
	}

	public Long countWrongQuiz(String id) {
		return wrongRepository.countByMemberId(id);
	}

	public Quiz wrongQuiz(String id, int q) {
		List<Wrong> w_quiz = wrongRepository.findByMemberId(id, PageRequest.of(q - 1, 1));
		QuizId quizId = new QuizId(w_quiz.get(0).getDay(), w_quiz.get(0).getQ());
		return quizRepository.findById(quizId).get();
	}
}
