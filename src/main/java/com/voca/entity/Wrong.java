package com.voca.entity;

import java.io.Serializable;
import javax.persistence.*;
import lombok.Data;

@Entity
@Data
@Table(uniqueConstraints = @UniqueConstraint(columnNames = { "memberid", "day", "q" }))
@IdClass(QuizId.class)
public class Wrong implements Serializable {
	
	@Column(name = "memberid")
	private String memberId;
	
	@Id
	private int day;
	
	@Id
	private int q;
	
	@ManyToOne
	@MapsId
	@JoinColumns(value = {@JoinColumn(name = "day"), @JoinColumn(name = "q")})
	private Quiz quiz;
	
}
