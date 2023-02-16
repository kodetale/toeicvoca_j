package com.voca.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.voca.entity.Quiz;
import com.voca.entity.QuizId;

public interface QuizRepository extends JpaRepository<Quiz, QuizId> {

	String[] findByCategory(String category);

	@Query("select distinct q.answer from Quiz q where q.category = :category")
	List<String> findEx(@Param("category") String category);
}
