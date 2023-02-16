package com.voca.repository;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.voca.entity.QuizId;
import com.voca.entity.Wrong;

public interface WrongRepository extends JpaRepository<Wrong, QuizId> {

	List<Wrong> findByMemberIdAndQuiz_Day(String id, int day, Pageable pageable);

	Wrong findByMemberIdAndQuiz_DayAndQuiz_Q(String id, int day, int q);

	List<Wrong> findByMemberId(String id, Pageable pageable);

	Long countByMemberIdAndQuiz_Day(String id, int day);

	Long countByMemberId(String id);

	Long deleteByMemberIdAndQuiz_DayAndQuiz_Q(String id, int day, int q);

	Long deleteByMemberId(String id);

}
