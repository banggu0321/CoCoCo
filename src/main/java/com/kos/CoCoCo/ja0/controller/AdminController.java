package com.kos.CoCoCo.ja0.controller;

import java.util.List;

import javax.servlet.http.HttpSession;
import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.kos.CoCoCo.ja0.repository.TeamRepository;
import com.kos.CoCoCo.ja0.repository.TeamUserRepository;
import com.kos.CoCoCo.ja0.repository.UserRepository;
import com.kos.CoCoCo.vo.TeamUserMultikey;
import com.kos.CoCoCo.vo.TeamUserVO;
import com.kos.CoCoCo.vo.TeamVO;
import com.kos.CoCoCo.vo.UserVO;

@Controller
@RequestMapping("/admin/*")
public class AdminController {

	@Autowired
	TeamUserRepository tuRepo;
	
	@Autowired
	TeamRepository tRepo;
	
	@Autowired
	UserRepository uRepo;

	@GetMapping("/user/{teamId}")
	public String userList(@PathVariable@ModelAttribute Long teamId, HttpSession session, Model model) {
		UserVO user = uRepo.findById("2ja0@naver.com").get();
		session.setAttribute("user", user);
		model.addAttribute("userList", tuRepo.findByTeamId(teamId));
		model.addAttribute("teamList", tuRepo.findByUserId(user.getUserId()));
		
		return "admin/adminUser";
	}
	
	@GetMapping("/deleteUser/{teamId}/{userId}")
	public String deleteUser(@PathVariable Long teamId, @PathVariable String userId, Model model) {
		TeamUserMultikey teamUser = TeamUserMultikey.builder().team(tRepo.findById(teamId).get())
												.user(uRepo.findById(userId).get()).build();
		tuRepo.deleteById(teamUser);
		
		return "redirect:/admin/user/"+teamId;
	}
	
	@GetMapping("/team/{teamId}")
	public String team(@PathVariable Long teamId, HttpSession session, Model model) {
		UserVO user = (UserVO)session.getAttribute("user");
		model.addAttribute("team", tRepo.findById(teamId).get());
		model.addAttribute("teamList", tuRepo.findByUserId(user.getUserId()));
		return "admin/adminTeam";
	}
	
	@GetMapping("/modify/{teamId}")
	public String modifyForm(@PathVariable Long teamId, Model model) {
		model.addAttribute("team", tRepo.findById(teamId).get());
		return "admin/modifyTeam";
	}
	
	@PostMapping("/modify")
	public String modifyTeam(TeamVO team, RedirectAttributes attr) {
		tRepo.findById(team.getTeamId()).ifPresent(i->{
			i.setTeamImg(team.getTeamImg());
			i.setTeamInfo(team.getTeamInfo());
			i.setTeamName(team.getTeamName());
			
			TeamVO result = tRepo.save(i);
			//attr.addFlashAttribute("msg", result !=null?"수정!":"실페!");
		});
		return "redirect:/admin/team/"+team.getTeamId();
	}
	
	@Transactional
	@GetMapping("/delete/{teamId}")
	public String deleteTeam(@PathVariable Long teamId, RedirectAttributes attr) {
		List<TeamUserVO> teamUser = tuRepo.findByTeamId(teamId);
		
		if(teamUser.size() <= 1) {
			tuRepo.deleteByTeamId(teamId);
			tRepo.deleteById(teamId);
			//attr.addFlashAttribute("msg", "워크스페이스가 삭제되었습니다.");			
			return "redirect:/main/teamList";
		}
		
		//attr.addFlashAttribute("msg", "워크스페이스를 삭제할 수 없습니다.");	
		return "redirect:/admin/team/"+teamId;
	}
}
