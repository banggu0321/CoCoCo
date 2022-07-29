package com.kos.CoCoCo.ja0.controller;

import java.io.IOException;
import java.security.Principal;
import java.util.List;
import java.util.Map;
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
import org.springframework.web.servlet.support.RequestContextUtils;

import com.kos.CoCoCo.fileUploader.S3Uploader;
import com.kos.CoCoCo.ja0.repository.TeamRepository;
import com.kos.CoCoCo.ja0.repository.TeamUserRepository;
import com.kos.CoCoCo.ja0.repository.UserRepository;
import com.kos.CoCoCo.vo.TeamUserMultikey;
import com.kos.CoCoCo.vo.TeamUserVO;
import com.kos.CoCoCo.vo.TeamVO;
import com.kos.CoCoCo.vo.UserVO;

@Controller
@RequestMapping("/main/*")
public class MainController {

	@Autowired
	TeamRepository tRepo;

	@Autowired
	TeamUserRepository tuRepo;
	
	@Autowired
	UserRepository uRepo;
	
	@Autowired
	S3Uploader uploader;
	
	@GetMapping("/teamList")
	public void teamList(HttpSession session, Model model, Principal principal, HttpServletRequest request) {
		Map<String, Object> map = (Map<String, Object>) RequestContextUtils.getInputFlashMap(request);
		
		if(map != null) {
			String msg = (String) map.get("msg");
			model.addAttribute("msg", msg);
		}
		
		UserVO user = uRepo.findById(principal.getName()).get();
		session.setAttribute("user", user);
		session.setAttribute("teamList", tuRepo.findByUserId(user.getUserId()));
	}
	
	@GetMapping("/{teamId}")
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
	
	@PostMapping("/addTeam")
	public String addTeam(TeamVO team, MultipartFile teamPhoto, HttpSession session) throws IOException{
		if (!teamPhoto.isEmpty()) {
			String img = uploader.upload(teamPhoto, "uploads/teamImages/");
			team.setTeamImg(img);
	    }
		
		UserVO user = (UserVO) session.getAttribute("user");
		
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
	
	@GetMapping("/deleteTeam/{teamId}")
	public String deleteTeam(@PathVariable Long teamId, HttpSession session) {
		TeamVO team = tRepo.findById(teamId).get();
		UserVO user = (UserVO) session.getAttribute("user");
		TeamUserMultikey teamUserId = new TeamUserMultikey(team, user);
		
		tuRepo.deleteById(teamUserId);
		
		return "redirect:/main/teamList";
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
			Long teamId = (Long) session.getAttribute("teamId");
			return "redirect:/main/"+teamId;
		}
		
		return "redirect:/main/teamList";
	}
	
	@ResponseBody
	@PostMapping("/findTeam/{code}")
	public Long findTeam(@PathVariable String code, HttpSession session, Model model) {
		List<TeamVO> team = tRepo.findByInviteCode(code);

		if(team.isEmpty()) return 0L;
		
		TeamVO t = team.get(0);
		UserVO user = (UserVO) session.getAttribute("user");
		TeamUserMultikey id = new TeamUserMultikey(t, user);
		tuRepo.findById(id).ifPresentOrElse(i->{
		}, ()->{
			TeamUserVO teamUser = TeamUserVO.builder().teamUserId(new TeamUserMultikey(t, user))
					.userRole("USER").build();
			tuRepo.save(teamUser);
		});
		
		session.setAttribute("teamList", tuRepo.findByUserId(user.getUserId()));
		
		return t.getTeamId();
	}
}
