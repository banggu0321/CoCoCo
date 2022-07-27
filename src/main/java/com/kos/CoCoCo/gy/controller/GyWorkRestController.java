package com.kos.CoCoCo.gy.controller;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.kos.CoCoCo.gy.repo.GyTeamRepository;
import com.kos.CoCoCo.gy.repo.GyTeamUserRepository;
import com.kos.CoCoCo.gy.repo.GyUserRepository;
import com.kos.CoCoCo.gy.repo.GyWorkManagerRepository;
import com.kos.CoCoCo.gy.repo.GyWorkRepository;
import com.kos.CoCoCo.vo.TeamUserMultikey;
import com.kos.CoCoCo.vo.TeamUserVO;
import com.kos.CoCoCo.vo.TeamVO;
import com.kos.CoCoCo.vo.UserVO;
import com.kos.CoCoCo.vo.WorkManagerMultikey;
import com.kos.CoCoCo.vo.WorkManagerVO;
import com.kos.CoCoCo.vo.WorkVO;

import javassist.expr.NewArray;

@RestController
@RequestMapping("/work/*")
public class GyWorkRestController {
	private static final Logger log = LoggerFactory.getLogger(GyWorkController.class);
	
	@Autowired
	GyTeamRepository teamRepo;

	@Autowired
	GyWorkRepository workRepo;
	
	@Autowired
	GyWorkManagerRepository workManagerRepo;
	
	@Autowired
	GyUserRepository userRepo;
	
	@Autowired
	GyTeamUserRepository teamUserRepo;
	//HttpSession session, HttpServletRequest request
	
	@GetMapping("/worklist.go/{team_id}")
	public List<WorkVO> worklist(Model model, @PathVariable Long team_id) {
		TeamVO team = teamRepo.findById(team_id).get();
		//System.out.println(team);
		List<WorkVO> worklist = workRepo.findByTeam(team);
		System.out.println(worklist);
		return worklist;
	}
	@PostMapping(value="/worklist.go/{team_id}", consumes = MediaType.APPLICATION_JSON_VALUE)
	public WorkVO addWork(@RequestBody WorkVO work , @PathVariable Long team_id) {
		TeamVO team = teamRepo.findById(team_id).get();
		
		log.info("Check: {}", work.toString());
		System.out.println(work);
		System.out.println(work.getWorkTitle());
		
		work.setTeam(team);
		WorkVO insertwork = workRepo.save(work);
		System.out.println(insertwork);
		
		for(String m:work.getManager()) {
			UserVO user = userRepo.findByName(m);
			WorkManagerMultikey multikey = new WorkManagerMultikey(insertwork, user);
			WorkManagerVO workmanager = new WorkManagerVO(multikey);
			WorkManagerVO insertworkmanager = workManagerRepo.save(workmanager);
			System.out.println(insertworkmanager);
		}
		
		//String a = work.keys("title")
		
		//Iterator i = ((Object) obj).keys();
		//WorkVO work = new WorkVO();
		//work.setWorkTitle(obj.getClass("title"))
		//WorkVO insertbBoard = workRepo.save(obj);
		return work;
	}
	@GetMapping("/workmanagerlist.go/{team_id}")
	public List<String> workmanagerlist(Model model, @PathVariable Long team_id) {
		//TeamVO team = teamRepo.findById(team_id).get();
		//System.out.println(team);
		List<TeamUserVO> user = teamUserRepo.findByTeam(team_id);
		//System.out.println(user);
		List<String> teamusernamelist = new ArrayList<>();
		
		for(TeamUserVO teamuser:user) {
			//System.out.println("담당자들2 : "+teamuser);
			UserVO uservo = teamuser.getTeamUserId().getUser();
			//System.out.println("담당자1" + uservo);
			teamusernamelist.add(uservo.getName());
		}
		System.out.println(teamusernamelist);
		return teamusernamelist;
	}
	@GetMapping(value="/workdetail.go/{work_id}")
	public WorkVO workdetaillist(Model model, @PathVariable Long work_id) {
		WorkVO work = workRepo.findByWorkId(work_id);
		List<WorkManagerVO> workmanagerlist = workManagerRepo.findByWork(work_id);
		List<String> list = new ArrayList();
		for(WorkManagerVO w:workmanagerlist) {
			
			String managerName = w.getWorkManagerId().getUser().getName(); 
			list.add(managerName);
			//work.setManager(list); 여기부터 고쳐야함 
			//work.manager값이 없음 string[] -> @Transient라서 문제인듯 
			//work.setManager(w.getWorkManagerId().getUser().getName());
			//list.add("item2");
			
			System.out.println(w.getWorkManagerId().getUser().getName());
		}
		//System.out.println(workmanagerlist);
		
		return work;
	}
	
	@PostMapping(value="/workdetail.go/{work_id}", consumes = MediaType.APPLICATION_JSON_VALUE)
	public WorkVO updateWork(@RequestBody WorkVO work , @PathVariable Long work_id) {
		//WorkVO work2 = workRepo.findById(work_id);
		
		
		//TeamVO team = teamRepo.findById(team_id).get();
		
		log.info("Check: {}", work.toString());
		System.out.println(work);
		System.out.println(work.getWorkTitle());
		
		//work.setTeam(team);
		WorkVO insertwork = workRepo.save(work);
		System.out.println(insertwork);
		
		//System.out.println(work.getManager());
		for(String m:work.getManager()) {
			UserVO user = userRepo.findByName(m);
			WorkManagerMultikey multikey = new WorkManagerMultikey(insertwork, user);
			WorkManagerVO workmanager = new WorkManagerVO(multikey);
			WorkManagerVO modifyworkmanager = workManagerRepo.save(workmanager);
			System.out.println(modifyworkmanager);
		}
		return work;
	}
}
