package com.kos.CoCoCo.ja0.controller;

import java.io.IOException;
import java.util.List;
import java.util.Random;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;


import com.kos.CoCoCo.ja0.repository.TeamRepository;
import com.kos.CoCoCo.ja0.repository.TeamUserRepository;
import com.kos.CoCoCo.ja0.repository.UserRepository;
import com.kos.CoCoCo.vo.TeamUserMultikey;
import com.kos.CoCoCo.vo.TeamUserVO;
import com.kos.CoCoCo.vo.TeamVO;
import com.kos.CoCoCo.vo.UserVO;

@Controller
@RequestMapping("/main/*")
public class TeamController {

	@Autowired
	TeamRepository tRepo;

	@Autowired
	TeamUserRepository tuRepo;
	
	@Autowired
	UserRepository uRepo;
	
	@Autowired
	S3Uploader uploader;
	
	@GetMapping("/teamList")
	public void teamList(HttpSession session, Model model) {
		UserVO user = uRepo.findById("2ja0@naver.com").get();
		session.setAttribute("user", user);
		
		model.addAttribute("teamList", tuRepo.findByUserId(user.getUserId()));
	}
	
	@GetMapping("/{teamId}")
	public String teamMain(@PathVariable Long teamId, HttpSession session, Model model) {
		UserVO user = (UserVO)session.getAttribute("user");
		model.addAttribute("team", tRepo.findById(teamId).get());
		model.addAttribute("userList", tuRepo.findByTeamId(teamId));
		model.addAttribute("teamList", tuRepo.findByUserId(user.getUserId()));
		return "main/teamMain";
	}
	
	@PostMapping("/addTeam/{userId}")
	public String addTeam(TeamVO team, MultipartFile teamPhoto, @PathVariable String userId, HttpServletRequest request) throws IOException{
		if (!teamPhoto.isEmpty()) {
			String img = uploader.upload(teamPhoto);
			team.setTeamImg(img);
	    }
		
		UserVO user = uRepo.findById(userId).get();
		
		team.setUser(user); 
		team.setInviteCode(makeTeamCode());
		TeamVO newTeam =  tRepo.save(team);
		TeamUserMultikey teamUserId = new TeamUserMultikey(newTeam, user);
		
		TeamUserVO teamUser = TeamUserVO.builder().teamUserId(teamUserId).userRole("ADMIN").build();
		tuRepo.save(teamUser);
		
		return "redirect:/main/teamList";
	}
	
	//초대코드 만들기
	public String makeTeamCode() {
		int leftLimit = 48;
		int rightLimit = 122;
		int targetStringLength = 20;
		Random random = new Random();
		String teamCode = random.ints(leftLimit, rightLimit + 1)
		                                   .filter(i -> (i <= 57 || i >= 65) && (i <= 90 || i >= 97))
		                                   .limit(targetStringLength)
		                                   .collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append)
		                                   .toString();
		return teamCode;
	}
	
	@GetMapping("/deleteTeam/{userId}/{teamId}")
	public String deleteTeam(@PathVariable Long teamId, @PathVariable String userId) {
		TeamVO team = tRepo.findById(teamId).get();
		UserVO user = uRepo.findById(userId).get();
		TeamUserMultikey teamUserId = new TeamUserMultikey(team, user);
		
		tuRepo.deleteById(teamUserId);
		
		return "redirect:/main/teamList";
	}
	
	@ResponseBody
	@PostMapping("/findTeam/{userId}/{code}")
	public Long findTeam(@PathVariable String code, @PathVariable String userId, Model model) {
		List<TeamVO> team = tRepo.findByInviteCode(code);

		if(team.isEmpty()) return 0L;
		
		TeamVO t = team.get(0);
		UserVO user = uRepo.findById(userId).get();
		TeamUserMultikey id = new TeamUserMultikey(t, user);
		tuRepo.findById(id).ifPresentOrElse(i->{
			
		}, ()->{
			TeamUserVO teamUser = TeamUserVO.builder().teamUserId(new TeamUserMultikey(t, user))
					.userRole("USER").build();
			tuRepo.save(teamUser);
		});
		
		return t.getTeamId();
	}
}
