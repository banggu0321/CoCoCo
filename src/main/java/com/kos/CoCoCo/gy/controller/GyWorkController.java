package com.kos.CoCoCo.gy.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.kos.CoCoCo.gy.repo.GyTeamRepository;
import com.kos.CoCoCo.gy.repo.GyWorkRepository;
import com.kos.CoCoCo.vo.TeamVO;
import com.kos.CoCoCo.vo.WorkVO;

import lombok.RequiredArgsConstructor;
import lombok.extern.java.Log;


//@Log
@Controller
@RequiredArgsConstructor
@RequestMapping("/work/*")
public class GyWorkController {
	
	private static final Logger log = LoggerFactory.getLogger(GyWorkController.class);
	
	@Autowired
	GyWorkRepository workRepo;
	
	@Autowired
	GyTeamRepository teamRepo;
	
	/*@GetMapping("/worklist.go")
	public String boardlist(Model model, HttpSession session, HttpServletRequest request) {
		return "work/worklist"; 
	}
	 */
	
	@GetMapping("/worklist.go")
	@ResponseBody
	public List<Map<String, Object>> boardlist(Model model,TeamVO team) {
		//model.addAttribute("wlist", workRepo.findByTeam(team));
		//workRepo.findById()
		//model.addAttribute("wmlist", teamRepo.findById(100L));
		//Model work = model.addAttribute("wlist",workRepo.findAll());
		//return work;
		List<WorkVO> listAll = (List<WorkVO>) workRepo.findAll();
		System.out.println(listAll);
		
        JSONObject jsonObj = new JSONObject();
        JSONArray jsonArr = new JSONArray();
 
        HashMap<String, Object> hash = new HashMap<>();
 
        for (int i = 0; i < listAll.size(); i++) {
            hash.put("title", listAll.get(i).getWorkTitle());
            hash.put("start", listAll.get(i).getWorkStart());
            hash.put("end", listAll.get(i).getWorkEnd());
            hash.put("status", listAll.get(i).getWorkStatus());
 
            jsonObj = new JSONObject(hash);
            jsonArr.add(jsonObj);
        }
        log.info("jsonArrCheck: {}", jsonArr);
        System.out.println(jsonArr);
        return jsonArr;
	}
	
	@PostMapping("/worklist.go")
	public String addWork() {
		
		return "ex/ex";
	}
	@GetMapping("/work/layout")
	public String layout() {
		return "work/layout/layout1";
	}
	@GetMapping("/work/workc")
	public String workCalendar() {
		return "work/calendar";
	}
}
