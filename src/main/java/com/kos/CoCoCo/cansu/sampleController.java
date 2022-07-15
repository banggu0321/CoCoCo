package com.kos.CoCoCo.cansu;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class sampleController {

	@GetMapping("/boardSample")
	public String boardlist() {
		return "su/boardMain";
	}
	
	@PostMapping("/boardInsertSample")
	public String boardInsert() {
		return "su/boardInsert";
	}
}
