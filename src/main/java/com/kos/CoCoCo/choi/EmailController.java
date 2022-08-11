package com.kos.CoCoCo.choi;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;


@RestController
public class EmailController { 
	
	@Autowired
	EmailService emailService;
	
	@GetMapping("/auth/emailConfirm")
    public String emailConfirm(  String email) throws Exception {
		 System.out.println("email:" + email);
        String confirm = emailService.sendSimpleMessage(email);
        System.out.println(confirm);
        return confirm;
    }

	
}