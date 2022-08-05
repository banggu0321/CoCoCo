package com.kos.CoCoCo.ja0.controller;

import java.io.IOException;
import java.util.List;

import javax.servlet.http.HttpSession;
import javax.transaction.Transactional;

import org.hibernate.internal.build.AllowSysOut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.kos.CoCoCo.ja0.awsS3.AwsS3;
import com.kos.CoCoCo.ja0.repository.BoardCategoryRepositoryH;
import com.kos.CoCoCo.ja0.repository.BoardRepositoryH;
import com.kos.CoCoCo.ja0.repository.ChattingRepositoryH;
import com.kos.CoCoCo.ja0.repository.NoticeRepositoryH;
import com.kos.CoCoCo.ja0.repository.ReplyRepositoryH;
import com.kos.CoCoCo.ja0.repository.TeamRepository;
import com.kos.CoCoCo.ja0.repository.TeamUserRepositoryH;
import com.kos.CoCoCo.ja0.repository.UserRepositoryH;
import com.kos.CoCoCo.ja0.repository.WorkManagerRepositoryH;
import com.kos.CoCoCo.ja0.repository.WorkRepositoryH;
import com.kos.CoCoCo.vo.TeamUserVO;
import com.kos.CoCoCo.vo.UserVO;
import com.kos.CoCoCo.vo.WorkManagerVO;

@Controller
public class UserController {

	@Autowired
	TeamUserRepositoryH tuRepo;
	
	@Autowired
	TeamRepository tRepo;
	
	@Autowired
	UserRepositoryH uRepo;
	
	@Autowired
	BoardCategoryRepositoryH bcRepo;
	
	@Autowired
	BoardRepositoryH bRepo;
	
	@Autowired
	ChattingRepositoryH cRepo;
	
	@Autowired
	NoticeRepositoryH nRepo;
	
	@Autowired
	ReplyRepositoryH rRepo;
	
	@Autowired
	WorkManagerRepositoryH wmRepo;
	
	@Autowired
	WorkRepositoryH wRepo;
	
	@Autowired
	AwsS3 awsS3;
	
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
	
	@PostMapping("/user/modify")
	public String modifyMyProfile(UserVO user, MultipartFile newPhoto, HttpSession session) {
		uRepo.findById(user.getUserId()).ifPresent(i->{
			if(!newPhoto.isEmpty()) {
				try {
					awsS3.delete(i.getImage()); //s3에서도 삭제
					
					String img = awsS3.upload(newPhoto, "uploads/userImages/");
					i.setImage(img);
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			
			i.setCompany(user.getCompany());
			i.setName(user.getName());
			
			uRepo.save(i);
			
			session.setAttribute("user", i);
		});
		
		return "redirect:/main";
	}
	
	@GetMapping("/user/deleteImg")
	public String deleteImg(HttpSession session) {
		UserVO user = (UserVO) session.getAttribute("user");
		
		uRepo.findById(user.getUserId()).ifPresent(i->{
			awsS3.delete(i.getImage()); //s3에서도 삭제
			
			i.setImage(null);
			uRepo.save(i);
			
			session.setAttribute("user", i);
		});
		
		return "redirect:/main";
	}
	
	@Transactional
	@GetMapping("/user/delete")
	public String deleteUser(HttpSession session, RedirectAttributes attr) {
		System.out.println("!!!탈퇴!!!");
		
		UserVO user = (UserVO) session.getAttribute("user");
		if(!adminCounter(user.getUserId())) {
			attr.addFlashAttribute("msg", "관리자 권한을 모두 넘긴 후 탈퇴가 가능합니다!");
		}
		
		changeAll(user.getUserId()); 
		
		/* if(user != null) */ return "redirect:/main";
		
		//return "redirect:/logout";
	}

	private boolean adminCounter(String userId) {
		List<TeamUserVO> adminTeam = tuRepo.findAdminByUserId(userId);
		
		if(!adminTeam.isEmpty()) {
			for(TeamUserVO t:adminTeam) {
				List<TeamUserVO> teamAdmin = tuRepo.findAdminByTeamId(t.getTeamUserId().getTeam().getTeamId());
				if(teamAdmin.size() == 1) return false;//관리자가 나밖에 없으면 탈퇴 불가
				
				UserVO userNone = uRepo.findById("XXXXX").get();
				for(TeamUserVO a:teamAdmin) {
					tRepo.findById(a.getTeamUserId().getTeam().getTeamId()).ifPresent(i->{
						String creator = i.getUser().getUserId();
						if(creator.equals(userId)) { //내가 만든 워크스페이스의 생성자 정보 변경
							i.setUser(userNone);
							tRepo.save(i);
						}
					});
				}
			}
		}
		return true;
	}
	
	private void changeAll(String userId) {
		UserVO userNone = uRepo.findById("XXXXX").get();
		
		//공지
		nRepo.findByUserId(userId).forEach(i->{
			i.setUser(userNone);
			nRepo.save(i);
		});
		
		//게시판
		bRepo.findByUserId(userId).forEach(i->{
			i.setUser(userNone);
			bRepo.save(i);
		});
		
		//게시판 댓글
		rRepo.findByUserId(userId).forEach(i->{
			i.setUser(userNone);
			rRepo.save(i);
		});
		
		//업무 담당자
		wmRepo.findByUserId(userId).forEach(i->{
			i.getWorkManagerId().setUser(userNone);
			wmRepo.save(i);
		});
		
		//채팅
		cRepo.findByUserId(userId).forEach(i->{
			i.setUser(userNone);
			cRepo.save(i);
		});
	}
}
