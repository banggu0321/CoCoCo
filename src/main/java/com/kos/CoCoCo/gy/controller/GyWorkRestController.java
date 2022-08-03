package com.kos.CoCoCo.gy.controller;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import javax.transaction.Transactional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
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

@RestController
@RequestMapping("/work/*")
public class GyWorkRestController {
	private static final Logger log = LoggerFactory.getLogger(GyWorkRestController.class);
	
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
	
	@GetMapping("/teamWorkList/{team_id}")
	public List<WorkVO> worklist(Model model, @PathVariable Long team_id) {
		TeamVO team = teamRepo.findById(team_id).get();
		//System.out.println(team);
		List<WorkVO> worklist = workRepo.findByTeam(team);
		
		for(WorkVO work:worklist) {
			List<WorkManagerVO> workmanagerlist = workManagerRepo.findByWork(work.getWorkId());
			String[] arr = new String[workmanagerlist.size()];
			for(int i=0;i<workmanagerlist.size();i++) {
				arr[i] = workmanagerlist.get(i).getWorkManagerId().getUser().getUserId();
			}
			work.setManager(arr);
		}
		System.out.println(worklist);
		return worklist;
	}
	@PostMapping(value="/addWork/{team_id}", consumes = MediaType.APPLICATION_JSON_VALUE)
	public WorkVO addWork(@RequestBody WorkVO work , @PathVariable Long team_id) {
		//System.out.println("오나요?");
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
		return work;
	}
	@GetMapping("/teamUserList/{team_id}")
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
	@GetMapping(value="/workDetail/{work_id}")
	public WorkVO workdetaillist(Model model, @PathVariable Long work_id) {
		WorkVO work = workRepo.findById(work_id).get();
		List<WorkManagerVO> workmanagerlist = workManagerRepo.findByWork(work_id);

		//(String[])workmanagerlist.toArray()
		//work.setManager(workmanagerlist.stream().toArray(String[]::new));
		String[] arr = new String[workmanagerlist.size()];
		for(int i=0;i<workmanagerlist.size();i++) {
			arr[i] = workmanagerlist.get(i).getWorkManagerId().getUser().getName();
		}
		work.setManager(arr);
		return work;
	}
	
	@Transactional
	@PutMapping(value="/updateWorkStatus/{work_id}", consumes = MediaType.APPLICATION_JSON_VALUE)
	public WorkVO updateStatusWork(@RequestBody String status , @PathVariable Long work_id) {
		WorkVO originalwork = workRepo.findById(work_id).get();
		originalwork.setWorkStatus(status);
		
		WorkVO updatework = workRepo.save(originalwork);
		return updatework;
	}
	@Transactional
	@PutMapping(value="/updateWork/{work_id}", consumes = MediaType.APPLICATION_JSON_VALUE)
	public WorkVO updateWork(@RequestBody WorkVO work , @PathVariable Long work_id) {
		
		//@PathVariable Long bno, @RequestBody WebReply reply
		//WebBoard board = boardRepo.findById(bno).get();
		//reply.setBoard(board);
		//System.out.println(reply);
		//replyRepo.save(reply);
		//return replyRepo.getRepliesOfBoard2(bno);
		
		
		WorkVO originalwork = workRepo.findById(work_id).get();
		originalwork.setWorkTitle(work.getWorkTitle());
		originalwork.setWorkText(work.getWorkText());
		originalwork.setWorkStart(work.getWorkStart());
		originalwork.setWorkEnd(work.getWorkEnd());
		originalwork.setWorkStatus(work.getWorkStatus());
		originalwork.setManager(work.getManager());
		
		WorkVO updatework = workRepo.save(originalwork);
		
		if(work.getManager()!=null) {
			workManagerRepo.workManagerDelete(work_id);
			for(String m:updatework.getManager()) {
				UserVO user = userRepo.findByName(m);
				WorkManagerMultikey multikey = new WorkManagerMultikey(updatework, user);
				WorkManagerVO workmanager = new WorkManagerVO(multikey);
				WorkManagerVO modifyworkmanager = workManagerRepo.save(workmanager);
				System.out.println(modifyworkmanager);
			}
		}else {
			System.out.println("Manager 변동없음");
		}
		return updatework;
	}
	
	@Transactional
	@DeleteMapping(value="/deleteWork/{work_id}")
	public void deleteWork(@PathVariable Long work_id) {
		workManagerRepo.workManagerDelete(work_id);
		workRepo.deleteById(work_id);
	}
	
	@GetMapping("/myWorkList/{user_id}")
	public List<WorkVO> myWork(Model model, @PathVariable String user_id) {
		//System.out.println("오긴함?");
		//UserVO user = userRepo.findById(user_id).get();
		List<WorkVO> myworklist = workRepo.findByUser(user_id);
		System.out.println(myworklist);
		return myworklist;
	}
	
	@GetMapping("/teamTodayWorkList/{team_id}")
	public List<WorkVO> todayWork(Model model, @PathVariable Long team_id) {
		TeamVO team = teamRepo.findById(team_id).get();
		//System.out.println(team);
		List<WorkVO> worklist = workRepo.findByTeam(team);
		
		for(WorkVO work:worklist) {
			List<WorkManagerVO> workmanagerlist = workManagerRepo.findByWork(work.getWorkId());
			String[] arr = new String[workmanagerlist.size()];
			for(int i=0;i<workmanagerlist.size();i++) {
				arr[i] = workmanagerlist.get(i).getWorkManagerId().getUser().getUserId();
			}
			work.setManager(arr);
		}
		System.out.println(worklist);
		return worklist;
	}
}
