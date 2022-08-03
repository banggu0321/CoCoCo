package com.kos.CoCoCo.sol.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.kos.CoCoCo.ja0.repository.TeamRepository;
import com.kos.CoCoCo.sol.repository.NoticeRepository;
import com.kos.CoCoCo.sol.repository.WorkRepository;
import com.kos.CoCoCo.vo.NoticeVO;
import com.kos.CoCoCo.vo.TeamVO;

@Controller
public class InnerMainController {
	
	@Autowired
	NoticeRepository noticeRepo;
	
	@Autowired
	TeamRepository tRepo;
	
	@Autowired
	WorkRepository wRepo;
	
	@GetMapping("/main/notice")
	public String mainNotice(Model model, NoticeVO notice, HttpSession session,
			HttpServletRequest requestPrinc) {
		
		Long teamId = (Long)session.getAttribute("teamId");
		TeamVO team = tRepo.findById(teamId).get();
		notice.setTeam(team);
		System.out.println("팀ID : "+ team.getTeamId());
		System.out.println(team);
		System.out.println(teamId);
		
		List<NoticeVO> noticelist = noticeRepo.findByTeamId(teamId);
				
		model.addAttribute("noticelist", noticelist);
		
		return "notice/main_notice";
	}
	
	@GetMapping("/main/todayworks")
	public String todayWorks(TeamVO team) {
		wRepo.findByTeam(team);
		
		return "work/main_todayworks";
	}

}
