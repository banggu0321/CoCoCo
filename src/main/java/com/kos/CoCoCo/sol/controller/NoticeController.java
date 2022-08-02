package com.kos.CoCoCo.sol.controller;

import java.security.Principal;
import java.util.List;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.kos.CoCoCo.ja0.repository.TeamRepository;
import com.kos.CoCoCo.ja0.repository.TeamUserRepository;
import com.kos.CoCoCo.ja0.repository.UserRepository;
import com.kos.CoCoCo.sol.repository.NoticeFileRepository;
import com.kos.CoCoCo.sol.repository.NoticeRepository;
import com.kos.CoCoCo.sol.service.NoticeService;
import com.kos.CoCoCo.sol.vo.NoticeFile;
import com.kos.CoCoCo.sol.vo.NoticePageVO;
import com.kos.CoCoCo.sol.vo.PageMaker;
import com.kos.CoCoCo.vo.NoticeVO;
import com.kos.CoCoCo.vo.TeamVO;
import com.kos.CoCoCo.vo.UserVO;
import com.querydsl.core.types.Predicate;

import lombok.extern.java.Log;

@Log
@Controller
@RequestMapping("/notice/*")
public class NoticeController {
	
	@Autowired
	TeamRepository tRepo;

	@Autowired
	TeamUserRepository tuRepo;
	
	@Autowired
	UserRepository uRepo;
	

	@Autowired
	NoticeService noticeService;
	
	@Autowired
	NoticeRepository noticeRepo;
	
	@Autowired
	NoticeFileRepository noticeFRepo;

	@GetMapping("/list")
	public String noticelist(NoticePageVO pageVO, Model model, HttpSession session,
			HttpServletRequest requestPrinc, Principal principal) {
		System.out.println("principal.getName():" + principal.getName());
		UserVO user =uRepo.findById(principal.getName()).get();
				
		TeamVO team = tRepo.findById(3L).get();
		session.setAttribute("teamId", 3L);
	    Long teamId = (Long)session.getAttribute("teamId");
		System.out.println("팀ID : "+ team.getTeamId());
		System.out.println(team);
		System.out.println(teamId);
		
		if(pageVO == null) {
			pageVO = new NoticePageVO();
		}
				
		Pageable page = pageVO.makePaging(0, new String[]{"fixedYN","noticeId"});
		Predicate prediacte = noticeRepo.makePredicate(pageVO.getType(), pageVO.getKeyword(), pageVO.getKeyword2());
		
		//notice.team.teamId가 session에서 얻은 team.teamId와 동일한 것만 출력 
		
	
		Page<NoticeVO> noticelist = noticeRepo.findAll(prediacte, page);
		
		//해당 team 게시판만 조회
		noticelist = new PageImpl<NoticeVO>(noticelist.stream()
				.filter(notice->{return notice.getTeam()!=null && notice.getTeam().getTeamId()==teamId;})
				.collect(Collectors.toList()),
				page, noticelist.getTotalElements());
		
		PageMaker<NoticeVO> pgmaker = new PageMaker<>(noticelist);
		
		model.addAttribute("noticelist", pgmaker);
		model.addAttribute("pageVO", pageVO);
		return "notice/noticelist";
	}
	
	
	@GetMapping("/detail")
	public String detail(NoticePageVO pageVO, Long noticeId, HttpSession session, Model model) {
		
		log.info("공지번호 : " + noticeId);
		
		model.addAttribute("pageVO", pageVO);
		model.addAttribute("notice", noticeRepo.findById(noticeId).get());
		
		
		List<NoticeFile> nflist = noticeFRepo.findByNoticeId(noticeId);
		System.out.println("nflist : " + nflist);
		
		if(nflist != null) {
			model.addAttribute("nflist", nflist);
		}
		
		return "notice/noticedetail";
	}
	
		
	
	@GetMapping("/insert")
	public String insert(HttpSession session, Principal principal) {
		
		return "notice/insertnotice";
	}
	
	@PostMapping("/insert")
	public String insertPost(HttpSession session, NoticeVO notice, MultipartFile[] files) throws Exception{
		
		/*TeamVO team = tRepo.findById(3L).get();
		session.setAttribute("teamId", 3L);
	    Long teamId = (Long)session.getAttribute("teamId");
		System.out.println("팀ID : "+ team.getTeamId());*/
		
		log.info(notice.toString());
		if(files == null ) {
			noticeService.insert(notice);
		} else {
			noticeService.insert(notice, files);
		}	
				
		return "redirect:/notice/list";
	}
	
	
	@GetMapping("/update")
	public String update(Model model, Long noticeId, @ModelAttribute NoticePageVO pageVO ) {
		model.addAttribute("notice", noticeRepo.findById(noticeId).get());
		return "notice/updatenotice";
	}
	
	@PostMapping("/update")
	public String modify(NoticeVO notice, Long noticeId, RedirectAttributes attr,
			@RequestParam("files") MultipartFile[] files, NoticeFile noticeFile , Model model) {
		noticeRepo.findById(notice.getNoticeId()).ifPresentOrElse(original->{
			original.setNoticeText(notice.getNoticeText());
			original.setNoticeTitle(notice.getNoticeTitle());
			original.setFixedYN(notice.getFixedYN());
			if(files==null) {
				NoticeVO updateNotice = noticeRepo.save(original);
			} else {
				try {
					noticeService.insert(original, files);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}, ()->{System.out.println("수정할 데이터 없음");});
		
				
		return "redirect:/notice/list";
	}
		
	

	@GetMapping("/delete")
	public String delete(NoticePageVO pageVO, NoticeVO notice, RedirectAttributes attr) {
		noticeRepo.deleteById(notice.getNoticeId());
		
		return "redirect:/notice/list";
	}
	


}

