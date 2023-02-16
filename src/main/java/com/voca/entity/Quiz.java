package com.voca.entity;

import java.io.Serializable;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.Table;
import com.voca.dto.QuizDto;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter @Setter
@Table(name = "quiz")
@IdClass(QuizId.class)
public class Quiz implements Serializable {
	
	@Id
	int day;
	
	@Id
	int q;
	
	private String voca;
	
	private String answer;
	
	private String category;
	
	public static Quiz createVoca(QuizDto quizDto) {
        Quiz quiz = new Quiz();
        quiz.setDay(quizDto.getDay());
        quiz.setQ(quizDto.getQ());
        quiz.setVoca(quizDto.getVoca());
        quiz.setAnswer(quizDto.getAnswer());
        quiz.setCategory(quizDto.getCategory()); 
        return quiz;
    }
	
}
