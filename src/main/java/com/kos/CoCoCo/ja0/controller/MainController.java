package com.kos.CoCoCo.ja0.controller;

import java.io.IOException;
import java.security.Principal;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.RequestContextUtils;

import com.kos.CoCoCo.fileUploader.S3Uploader;
import com.kos.CoCoCo.ja0.VO.PageMaker;
import com.kos.CoCoCo.ja0.VO.PageVO;
import com.kos.CoCoCo.ja0.repository.TeamRepository;
import com.kos.CoCoCo.ja0.repository.TeamUserRepositoryH;
import com.kos.CoCoCo.ja0.repository.UserRepositoryH;
import com.kos.CoCoCo.vo.TeamUserMultikey;
import com.kos.CoCoCo.vo.TeamUserVO;
import com.kos.CoCoCo.vo.TeamVO;
import com.kos.CoCoCo.vo.UserVO;
import com.querydsl.core.types.Predicate;

@Controller
public class MainController {

	@Autowired
	TeamRepository tRepo;

	@Autowired
	TeamUserRepositoryH tuRepo;
	
	@Autowired
	UserRepositoryH uRepo;
	
	@Autowired
	S3Uploader uploader;
	
	@GetMapping("/CoCoCo")
	public String teamList(@ModelAttribute PageVO pageVO, HttpSession session, Model model, Principal principal, HttpServletRequest request) {
		Map<String, Object> map = (Map<String, Object>) RequestContextUtils.getInputFlashMap(request);
		
		if(map != null) {
			String msg = (String) map.get("msg");
			model.addAttribute("msg", msg);
		}
		
		UserVO user = uRepo.findById(principal.getName()).get();
		List<TeamUserVO> teamList = tuRepo.findByUserId(user.getUserId());
		session.setAttribute("user", user);
		session.setAttribute("teamList", teamList);
		
		//teamListPaging
		Pageable page = pageVO.makePaging(1, "joinDate"); //joinDate로 asc
		Predicate predicate =  tuRepo.makePredicate(user.getUserId());
		
		Page<TeamUserVO> team = tuRepo.findAll(predicate, page);
		
		PageMaker<TeamUserVO> pgmaker = new PageMaker<>(team);
		model.addAttribute("paging", pgmaker);
		
		return "/main/teamList";
	}
	
	@PostMapping("/addTeam")
	public String addTeam(TeamVO team, MultipartFile teamPhoto, HttpSession session) throws IOException{
		if (!teamPhoto.isEmpty()) {
			String img = uploader.upload(teamPhoto, "uploads/teamImages/");
			team.setTeamImg(img);
	    }
		
		UserVO user = (UserVO) session.getAttribute("user");
		
		team.setUser(user); 
		team.setInviteCode(UUID.randomUUID().toString()); 
		TeamVO newTeam =  tRepo.save(team);
		TeamUserMultikey teamUserId = new TeamUserMultikey(newTeam, user);
		
		TeamUserVO teamUser = TeamUserVO.builder().teamUserId(teamUserId).userRole("ADMIN").build();
		tuRepo.save(teamUser);
		
		return "redirect:/CoCoCo";
	}
	
	@GetMapping("/deleteTeam/{teamId}")
	public String deleteTeam(@PathVariable Long teamId, HttpSession session) {
		TeamVO team = tRepo.findById(teamId).get();
		UserVO user = (UserVO) session.getAttribute("user");
		TeamUserMultikey teamUserId = new TeamUserMultikey(team, user);
		
		tuRepo.deleteById(teamUserId);
		
		return "redirect:/CoCoCo";
	}
	
	@GetMapping("/updateStatus/{str}/{status}")
	public String updateStatus(@PathVariable String str, @PathVariable String status, HttpSession session) {
		UserVO user = (UserVO) session.getAttribute("user");
		
		uRepo.findById(user.getUserId()).ifPresent(i->{
			i.setStatus(status);
			uRepo.save(i);
			session.setAttribute("user", i);
		});
		
		if(str.equals("t")) {
			return "redirect:/main";
		}
		
		return "redirect:/CoCoCo";
	}
	
	@ResponseBody
	@PostMapping("/findTeam/{code}")
	public Long findTeam(@PathVariable String code, HttpSession session, Model model) {
		TeamVO team = tRepo.findByInviteCode(code);

		if(team == null) return 0L;
		
		UserVO user = (UserVO) session.getAttribute("user");
		TeamUserMultikey id = new TeamUserMultikey(team, user);
		
		tuRepo.findById(id).ifPresentOrElse(i->{
		}, ()->{
			TeamUserVO teamUser = TeamUserVO.builder().teamUserId(new TeamUserMultikey(team, user))
					.userRole("USER").build();
			tuRepo.save(teamUser);
		});
		
		session.setAttribute("teamList", tuRepo.findByUserId(user.getUserId()));
		session.setAttribute("teamId", team.getTeamId());
		
		return team.getTeamId();
	}
	
	@ResponseBody
	@GetMapping("/setTeamId/{teamId}")
	public void setTeamId(@PathVariable Long teamId, HttpSession session) {
		session.setAttribute("teamId", teamId);
		System.out.println("세션에 teamId 저장!!");
	}
	
	@GetMapping("/main")
	public String teamMain(HttpSession session, Model model) {
		UserVO user = (UserVO)session.getAttribute("user");
		Long teamId = (Long) session.getAttribute("teamId");
		TeamVO team = tRepo.findById(teamId).get();
		TeamUserMultikey tuId = new TeamUserMultikey(team, user);
		
		session.setAttribute("teamUser", tuRepo.findById(tuId).get());
		model.addAttribute("team", team);
		model.addAttribute("userList", tuRepo.findByTeamId(teamId));
		
		return "main/teamMain";
	}
}
