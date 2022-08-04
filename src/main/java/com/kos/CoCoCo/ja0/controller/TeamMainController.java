package com.kos.CoCoCo.ja0.controller;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import com.kos.CoCoCo.ja0.repository.TeamRepository;
import com.kos.CoCoCo.ja0.repository.TeamUserRepository;
import com.kos.CoCoCo.vo.TeamUserMultikey;
import com.kos.CoCoCo.vo.TeamVO;
import com.kos.CoCoCo.vo.UserVO;

@Controller
//@RequestMapping("/main/*")
public class TeamMainController {

	@Autowired
	TeamRepository tRepo;

	@Autowired
	TeamUserRepository tuRepo;
	
	//@GetMapping("/{teamId}")
	public String teamMain(@PathVariable Long teamId, HttpSession session, Model model) {
		UserVO user = (UserVO)session.getAttribute("user");
		TeamVO team = tRepo.findById(teamId).get();
		TeamUserMultikey tuId = new TeamUserMultikey(team, user);
		
		session.setAttribute("teamId", teamId);
		session.setAttribute("teamUser", tuRepo.findById(tuId).get());
		model.addAttribute("team", team);
		model.addAttribute("userList", tuRepo.findByTeamId(teamId));
		
		return "main/teamMain";
	}
}
