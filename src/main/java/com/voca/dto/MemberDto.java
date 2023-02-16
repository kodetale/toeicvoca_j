package com.voca.dto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;

import org.hibernate.validator.constraints.Length;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MemberDto {

	@NotBlank
	@Length(min = 4, max = 12)
	@Pattern(regexp = "[a-zA-Z0-9]+")
	private String memberId;

	@NotEmpty
	@Length(min = 8, max = 16)
	private String password;

	@NotEmpty
	@Length(max = 8)
	private String name;

}