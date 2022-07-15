package com.kos.CoCoCo.cansu;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class sampleController {

	@GetMapping("/boardSample")
	public String boardList() {
		return "cansu/boardMain";
	}
}
