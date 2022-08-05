package com.kos.CoCoCo.ja0.controller;

import java.io.IOException;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.multipart.MultipartFile;

import com.kos.CoCoCo.ja0.awsS3.AwsS3;
import com.kos.CoCoCo.ja0.repository.TeamRepository;
import com.kos.CoCoCo.ja0.repository.TeamUserRepositoryH;
import com.kos.CoCoCo.ja0.repository.UserRepositoryH;
import com.kos.CoCoCo.vo.TeamVO;
import com.kos.CoCoCo.vo.UserVO;

@Controller
public class UserController {

	@Autowired
	TeamRepository tRepo;

	@Autowired
	TeamUserRepositoryH tuRepo;
	
	@Autowired
	UserRepositoryH uRepo;
	
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
			
			i.setImage("");
			uRepo.save(i);
			
			session.setAttribute("user", i);
		});
		
		return "redirect:/main";
	}
	
	@GetMapping("/user/delete")
	public String deleteUser(HttpSession session) {
		
		return "";
	}
}
