package com.kos.CoCoCo.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.kos.CoCoCo.vo.UserVO;

//config에 요청이있다면 RequestMapping은 필수
@Controller
public class SecurityController {

	@Autowired
	MemberService mservice;

	//필수 
	@GetMapping("/auth/login")
	public void login() {
		System.out.println("/auth/login   Get");
	}

	@GetMapping("/logout")
	public void logout() {
		
	}

	@RequestMapping("/auth/loginSuccess")
	public void loginSuccess() {
		System.out.println("/auth/loginSuccess");
	}
	@RequestMapping("/accessDenied")
	public String accessDenied() {
		System.out.println("/auth/accessDenied");
		return "auth/accessDenied";
	}
	@GetMapping("/auth/join")
	public String join() {
	  return "auth/joinForm";	
	}
	@PostMapping("/auth/joinProc")
	public String register(@ModelAttribute UserVO member) {
		System.out.println("===회원가입하기====");
		mservice.joinUser(member);
	  return "redirect:/auth/login";	
	}
}
