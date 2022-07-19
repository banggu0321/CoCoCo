package com.kos.CoCoCo.gy.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.kos.CoCoCo.gy.repo.GyTeamRepository;
import com.kos.CoCoCo.gy.repo.GyWorkRepository;
import com.kos.CoCoCo.vo.TeamVO;

import lombok.extern.java.Log;

@Log
@Controller
@RequestMapping("/work/*")
public class GyWorkController {
	
	@Autowired
	GyWorkRepository workRepo;
	
	@Autowired
	GyTeamRepository teamRepo;
	
	/*@GetMapping("/worklist.go")
	public String boardlist(Model model, HttpSession session, HttpServletRequest request) {
		return "work/worklist"; 
	}
	 */
	
	@GetMapping("/worklist.go")
	public String boardlist(Model model,TeamVO team) {
		//model.addAttribute("wlist", workRepo.findByTeam(team));
		model.addAttribute("wlist",workRepo.findAll());
		//workRepo.findById()
		model.addAttribute("wmlist", teamRepo.findById(100L));
		return "work/worklist";
	}
	@GetMapping("/work/layout")
	public String layout() {
		return "work/layout/layout1";
	}
}
