package com.kos.CoCoCo.gy.controller;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.kos.CoCoCo.gy.repo.GyTeamRepository;
import com.kos.CoCoCo.gy.repo.GyTeamUserRepository;
import com.kos.CoCoCo.gy.repo.GyWorkManagerRepository;
import com.kos.CoCoCo.gy.repo.GyWorkRepository;

//@Log
@Controller
@RequestMapping("/work/*")
public class GyWorkController {
	
	private static final Logger log = LoggerFactory.getLogger(GyWorkController.class);
	
	@Autowired
	GyWorkRepository workRepo;
	
	@Autowired
	GyTeamRepository teamRepo;
	
	@Autowired
	GyWorkManagerRepository workmanagerRepo;
	
	@Autowired
	GyTeamUserRepository teamuserRepo;
	
	@GetMapping("/workc")
	public String workCalendar() {
		return "/work/calendar";
	}
	 
	
	@GetMapping("/work/layout")
	public String layout() {
		return "work/layout/layout1";
	}
	
}