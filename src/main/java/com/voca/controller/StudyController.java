package com.voca.controller;

import java.security.Principal;

import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.voca.entity.Member;
import com.voca.entity.Quiz;
import com.voca.service.MemberService;
import com.voca.service.StudyService;

import lombok.RequiredArgsConstructor;

@RequestMapping("/study")
@Controller
@RequiredArgsConstructor
public class StudyController {

	private final MemberService memberService;
	private final StudyService studyService;
	private final HttpSession session;
	private int cnt;

	public String name(Principal principal) {
		String name = null;
		if (principal != null) {
			String loginId = principal.getName();
			Member member = memberService.findbyMemberId(loginId);
			name = member.getName();
		}

		return name;
	}

	@GetMapping(value = "/option")
	public String option(@RequestParam(value = "day") int day, Model model, Principal principal) {
		model.addAttribute("name", name(principal));
		model.addAttribute("day", day);
		return "study/option";
	}

	@GetMapping(value = "/quizA")
	public String quizA(@RequestParam(value = "day") int day, @RequestParam(value = "q") int q, Model model,
			Principal principal) {
		if (q == 1) {
			cnt = 0;
		}

		model.addAttribute("name", name(principal));
		model.addAttribute("day", day);
		model.addAttribute("q", q);
		Quiz quiz = studyService.findbyQuizId(day, q);
		model.addAttribute("quiz", quiz);
		model.addAttribute("ex", studyService.Example(quiz.getAnswer(), quiz.getCategory()));
		return "study/quizA";
	}

	@ResponseBody
	@RequestMapping(value = "/quizA", method = { RequestMethod.POST })
	public String correctAns(String ex, String answer, int day, int q, Principal principal) {
		String s = studyService.correctAns(answer, ex);
		if (s == "o") {
			session.setAttribute("cnt", ++cnt);
		} else {
			studyService.saveWrong(principal.getName(), day, q);
		}

		return s;
	}

	@GetMapping(value = "/quizB")
	public String quizB(@RequestParam(value = "day") int day, @RequestParam(value = "q") int q, Model model,
			Principal principal) {
		model.addAttribute("name", name(principal));
		model.addAttribute("day", day);
		model.addAttribute("q", q);
		Quiz quiz = studyService.findbyQuizId(day, q);
		model.addAttribute("quiz", quiz);
		return "study/quizB";
	}

	@ResponseBody
	@RequestMapping(value = "/quizB", method = { RequestMethod.POST })
	public void addWrong(int day, int q, Principal principal) {
		studyService.saveWrong(principal.getName(), day, q);
	}

	@GetMapping(value = "/result")
	public String result(@RequestParam(value = "day") int day, Model model, Principal principal) {
		model.addAttribute("name", name(principal));
		model.addAttribute("day", day);
		model.addAttribute("cnt", session.getAttribute("cnt"));
		session.removeAttribute("cnt");
		return "study/result";
	}

	@GetMapping(value = "/wrong")
	public String wrong(@RequestParam(value = "day") int day, @RequestParam(value = "page") int page, Model model,
			Principal principal) {
		model.addAttribute("name", name(principal));
		model.addAttribute("day", day);
		model.addAttribute("page", page);

		int maxPage = 4;
		int block = (int) Math.ceil(page / 4d);
		int start = ((block - 1) * maxPage) + 1;
		int end = start + maxPage - 1;
		int total = (int) Math.ceil(studyService.countWrong(principal.getName(), day) / 10d);
		if (end > total)
			end = total;

		model.addAttribute("start", start);
		model.addAttribute("end", end);
		model.addAttribute("quiz", studyService.printWrong(principal.getName(), day, page));
		return "study/wrong";
	}

	@ResponseBody
	@RequestMapping(value = "/wrong", method = { RequestMethod.POST })
	public void deleteWrong(int day, int q, Principal principal) {
		studyService.deleteWrong(principal.getName(), day, q);
	}

	@GetMapping(value = "/wrongoption")
	public String wrongoption(Model model, Principal principal) {
		model.addAttribute("name", name(principal));
		return "study/wrongoption";
	}

	@GetMapping(value = "/wrongquizA")
	public String wrongquizA(@RequestParam(value = "q") int q, Model model, Principal principal) {
		if (q == 1) {
			cnt = 0;
		}

		model.addAttribute("name", name(principal));
		model.addAttribute("q", q);
		model.addAttribute("total", studyService.countWrongQuiz(principal.getName()));
		Quiz quiz = studyService.findbyQuizId((studyService.wrongQuiz(principal.getName(), q)).getDay(),
				studyService.wrongQuiz(principal.getName(), q).getQ());
		model.addAttribute("quiz", studyService.wrongQuiz(principal.getName(), q));
		model.addAttribute("ex", studyService.Example(quiz.getAnswer(), quiz.getCategory()));
		return "study/wrongquizA";
	}

	@GetMapping(value = "/wrongquizB")
	public String wrongquizB(@RequestParam(value = "q") int q, Model model, Principal principal) {

		model.addAttribute("name", name(principal));
		model.addAttribute("q", q);
		model.addAttribute("total", studyService.countWrongQuiz(principal.getName()));
		model.addAttribute("quiz", studyService.wrongQuiz(principal.getName(), q));
		return "study/wrongquizB";
	}

	@GetMapping(value = "/wrongresult")
	public String wrongresult(Model model, Principal principal) {
		model.addAttribute("name", name(principal));
		model.addAttribute("cnt", session.getAttribute("cnt"));
		session.removeAttribute("cnt");
		return "study/wrongresult";
	}

}