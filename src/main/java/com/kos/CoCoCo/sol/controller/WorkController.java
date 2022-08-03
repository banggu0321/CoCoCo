package com.kos.CoCoCo.sol.controller;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import com.kos.CoCoCo.gy.repo.GyTeamRepository;
import com.kos.CoCoCo.gy.repo.GyTeamUserRepository;
import com.kos.CoCoCo.gy.repo.GyUserRepository;
import com.kos.CoCoCo.gy.repo.GyWorkManagerRepository;
import com.kos.CoCoCo.gy.repo.GyWorkRepository;
import com.kos.CoCoCo.vo.TeamVO;
import com.kos.CoCoCo.vo.WorkManagerVO;
import com.kos.CoCoCo.vo.WorkVO;

@Controller
@RequestMapping("/")
public class WorkController {
	
	@Autowired
	GyTeamRepository teamRepo;

	@Autowired
	GyWorkRepository workRepo;
	
	@Autowired
	GyWorkManagerRepository workManagerRepo;
	
	@Autowired
	GyUserRepository userRepo;
	
	@Autowired
	GyTeamUserRepository teamUserRepo;
	
	
	@GetMapping("/work/main/{team_id}")
	public String mainWorklist(Model model, @PathVariable Long team_id) {
		TeamVO team = teamRepo.findById(team_id).get();
		//System.out.println(team);
		List<WorkVO> worklist = workRepo.findByTeam(team);
		
		model.addAttribute("worklist", worklist);
		model.addAttribute("localDateTime", LocalDateTime.now());
		
		return "work/main_work";
	}
}
