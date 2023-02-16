package com.voca.controller;

import java.security.Principal;
import javax.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import com.voca.entity.Member;
import com.voca.service.MemberService;
import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class MainController {
	
	private final MemberService memberService;
	private final HttpSession session;
	
	@GetMapping(value = "/")
	public String main(Principal principal, Model model) {
		if(principal != null) {
			String loginId = principal.getName();
        	Member member = memberService.findbyMemberId(loginId);
        	model.addAttribute("name", member.getName());
        	session.removeAttribute("cnt");
        }
		
	    return "main";
	}
	
}