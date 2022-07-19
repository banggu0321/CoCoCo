package com.kos.CoCoCo.ja0;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/admin/*")
public class AdminController {

	@Autowired
	UserRepository uRepo;
	
	@GetMapping("/admin")
	public void test(Model model) {
		model.addAttribute("userList", uRepo.findAll());
		System.out.println(uRepo.findAll());
	}
}
