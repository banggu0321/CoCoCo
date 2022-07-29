package com.kos.CoCoCo.ja0.controller;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
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
@RequestMapping("/admin/*")
public class AdminController {

	@Autowired
	TeamUserRepository tuRepo;
	
	@Autowired
	TeamRepository tRepo;
	
	@Autowired
	UserRepository uRepo;
	
	@Autowired
	S3Uploader uploader;

	@GetMapping("/user")
	public String userList(HttpSession session, Model model) {
		Long teamId = (Long) session.getAttribute("teamId");
		model.addAttribute("team", tRepo.findById(teamId).get()); //title
		model.addAttribute("userList", tuRepo.findByTeamId(teamId));
		
		return "admin/adminUser";
	}
	
	@GetMapping("/deleteUser/{userId}")
	public String deleteUser(@PathVariable String userId, HttpSession session, Model model) {
		Long teamId = (Long) session.getAttribute("teamId");
		TeamUserMultikey teamUser = TeamUserMultikey.builder().team(tRepo.findById(teamId).get())
												.user(uRepo.findById(userId).get()).build();
		tuRepo.deleteById(teamUser);
		
		return "redirect:/admin/user";
	}
	
	@GetMapping("/team")
	public String team(HttpSession session, Model model, HttpServletRequest request) {
		Long teamId = (Long) session.getAttribute("teamId");
		
		Map<String, Object> map = (Map<String, Object>) RequestContextUtils.getInputFlashMap(request);
		if(map != null) {
			String msg = (String) map.get("msg");
			model.addAttribute("msg", msg);
		}
		
		model.addAttribute("team", tRepo.findById(teamId).get());
		return "admin/adminTeam";
	}
	
	@GetMapping("/modify")
	public String modifyForm(HttpSession session, Model model) {
		Long teamId = (Long) session.getAttribute("teamId");
		model.addAttribute("team", tRepo.findById(teamId).get());
		return "admin/modifyTeam";
	}
	
	@PostMapping("/modify")
	public String modifyTeam(TeamVO team, MultipartFile newPhoto, HttpSession session) {
		tRepo.findById(team.getTeamId()).ifPresent(i->{		
			if (!newPhoto.isEmpty()) {
				try {
					String img = uploader.upload(newPhoto, "uploads/teamImages/");
					i.setTeamImg(img);
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			
			i.setTeamInfo(team.getTeamInfo());
			i.setTeamName(team.getTeamName());
			
			tRepo.save(i);
		});
		
		UserVO user = (UserVO) session.getAttribute("user");
		session.setAttribute("teamList", tuRepo.findByUserId(user.getUserId()));
		
		return "redirect:/admin/team";
	}
	
	@GetMapping("/deleteImg")
	public String deleteImg(HttpSession session) {
		Long teamId = (Long) session.getAttribute("teamId");
		
		tRepo.findById(teamId).ifPresent(i->{
			i.setTeamImg("");
			tRepo.save(i);
		});
		
		return "redirect:/admin/team";
	}
	
	@Transactional
	@GetMapping("/delete")
	public String deleteTeam(HttpSession session, RedirectAttributes attr) {
		Long teamId = (Long) session.getAttribute("teamId");
		List<TeamUserVO> teamUser = tuRepo.findByTeamId(teamId);
		
		if(teamUser.size() <= 1) {
			//tuRepo.deleteByTeamId(teamId);
			tRepo.deleteById(teamId);
			attr.addFlashAttribute("msg", "워크스페이스가 삭제되었습니다.");			
			return "redirect:/main/teamList";
		}
		
		attr.addFlashAttribute("msg", "워크스페이스를 삭제할 수 없습니다.");	
		return "redirect:/admin/team";
	}
	
	@ResponseBody
	@PostMapping("/findUser/{userId}")
	public TeamUserVO findUser(@PathVariable String userId, HttpSession session) {
		Long teamId = (Long) session.getAttribute("teamId");
		
		TeamUserMultikey id = new TeamUserMultikey(tRepo.findById(teamId).get(), uRepo.findById(userId).get());
		return tuRepo.findById(id).get();
	}
	
	@GetMapping("/updateUser/{userId}/{newRole}")
	public String updateUser(@PathVariable String userId, @PathVariable String newRole, HttpSession session) {
		Long teamId = (Long) session.getAttribute("teamId");
		
		TeamUserMultikey id = new TeamUserMultikey(tRepo.findById(teamId).get(), uRepo.findById(userId).get());
		tuRepo.findById(id).ifPresent(i->{
			i.setUserRole(newRole);
			tuRepo.save(i);
		});
		return "redirect:/admin/user";
	}
}
