package com.voca.controller;

import java.security.Principal;

import javax.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.voca.dto.MemberDto;
import com.voca.entity.Member;
import com.voca.service.MemberService;

import lombok.RequiredArgsConstructor;

@RequestMapping("/member")
@Controller
@RequiredArgsConstructor
public class MemberController {

	private final MemberService memberService;
	private final PasswordEncoder passwordEncoder;

	@GetMapping(value = "/join")
	public String joinMember(Model model) {
		model.addAttribute("memberDto", new MemberDto());
		return "member/join";
	}

	@PostMapping(value = "/join")
	public String joinMember(@Valid MemberDto memberDto, BindingResult bindingResult, Model model) {
		if (bindingResult.hasErrors()) {
			return "member/join";
		}

		try {
			Member member = Member.createMember(memberDto, passwordEncoder);
			memberService.saveMember(member);
		} catch (IllegalStateException e) {
			model.addAttribute("errorMessage", e.getMessage());
			return "member/join";
		}

		model.addAttribute("msg", "회원가입이 완료되었습니다.");
		model.addAttribute("url", "/");

		return "member/join";
	}

	@GetMapping(value = "/edit")
	public String editMember(Model model, Principal principal) {
		String loginId = principal.getName();
		Member member = memberService.findbyMemberId(loginId);
		String name = member.getName();
		model.addAttribute("member", member);
		model.addAttribute("name", name);
		return "member/edit";
	}

	@ResponseBody
	@RequestMapping(value = "/edit", method = { RequestMethod.PUT })
	public ResponseEntity<Member> editMember(Member member) {
		memberService.editMember(member, passwordEncoder);
		return new ResponseEntity<>(member, HttpStatus.OK);
	}

	@ResponseBody
	@RequestMapping(value = "/edit", method = { RequestMethod.POST })
	public ResponseEntity<Member> delMember(Member member) {
		memberService.delMember(member);
		SecurityContextHolder.clearContext();
		return new ResponseEntity<>(member, HttpStatus.OK);
	}

	@GetMapping(value = "/login")
	public String loginMember() {
		return "/member/login";
	}

	@GetMapping(value = "/login/error")
	public String loginError(Model model) {
		model.addAttribute("loginErrorMsg", "아이디 또는 비밀번호를 확인해주세요");
		return "/member/login";
	}

}