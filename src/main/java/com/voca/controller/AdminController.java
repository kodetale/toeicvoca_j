package com.voca.controller;

import com.voca.dto.QuizDto;
import com.voca.entity.Member;
import com.voca.entity.Quiz;
import com.voca.service.AdminService;
import com.voca.service.MemberService;
import java.security.Principal;
import javax.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import lombok.RequiredArgsConstructor;

@RequestMapping("/admin")
@Controller
@RequiredArgsConstructor
public class AdminController {
	
	private final MemberService memberService;
	private final AdminService adminService;
	private final HttpServletRequest request;
	
	public String name(Principal principal) {
		String name = null;
		
		if(principal != null) {
			String loginId = principal.getName();
			Member member = memberService.findbyMemberId(loginId);
			name = member.getName();
		}
		
		return name;
	}
	
	@GetMapping(value = "/voca")
	public String quiz(@RequestParam(value = "day") int day, Model model, Principal principal) {
		model.addAttribute("name", name(principal));
		model.addAttribute("day", day);
		model.addAttribute("quizDto", new QuizDto());
		return "admin/voca";
	}
	
	@PostMapping(value = "/voca")
    public String createVoca(QuizDto quizDto) {
		Quiz quiz = Quiz.createVoca(quizDto);
        adminService.saveVoca(quiz);
        String referer = request.getHeader("Referer");
        return "redirect:"+ referer;
	}

}
