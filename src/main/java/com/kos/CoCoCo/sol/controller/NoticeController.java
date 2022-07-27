package com.kos.CoCoCo.sol.controller;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.kos.CoCoCo.sol.repository.NoticeRepository;
import com.kos.CoCoCo.sol.vo.NoticePageVO;
import com.kos.CoCoCo.sol.vo.PageMaker;
import com.kos.CoCoCo.vo.NoticeVO;
import com.querydsl.core.types.Predicate;

import lombok.extern.java.Log;

@Log
@Controller
@RequestMapping("/notice/*")
public class NoticeController {
	
	@Autowired
	NoticeRepository noticeRepo;

	@GetMapping("/list")
	public String noticelist(NoticePageVO pageVO, Model model, HttpSession session,
			HttpServletRequest request) {
		if(pageVO == null) {
			pageVO = new NoticePageVO();
		}
		System.out.println(pageVO);
		
		Pageable page = pageVO.makePaging(0, "noticeId");
		Predicate prediacte = noticeRepo.makePredicate(pageVO.getType(), pageVO.getKeyword(), pageVO.getKeyword2());
		
		Page<NoticeVO> noticelist = noticeRepo.findAll(prediacte, page);
		PageMaker<NoticeVO> pgmaker = new PageMaker<>(noticelist);
		
		model.addAttribute("noticelist", pgmaker);
		model.addAttribute("pageVO", pageVO);
		return "notice/noticelist";
	}
	
	
	
	@GetMapping("/detail")
	public String detail(NoticePageVO pageVO, Long noticeId, Model model) {
		log.info(pageVO.toString());
		log.info("공지번호 : " + noticeId);
		
		model.addAttribute("notice", noticeRepo.findById(noticeId).get());
		model.addAttribute("pageVO", pageVO);
		return "notice/noticedetail";
	}
	
		
	
	@GetMapping("/insert")
	public String insert() {
		return "notice/insertnotice";
	}
	
	@PostMapping("/insert")
	public String insertPost(NoticeVO notice, RedirectAttributes attr) {
		log.info(notice.toString());
		
		NoticeVO insertNotice = noticeRepo.save(notice);
		attr.addFlashAttribute("msg", insertNotice!=null?"insert success":"insert failed");
		
		return "redirect:/notice/list";
	}
	
	
	
	@GetMapping("/update")
	public String modify(Model model, Long noticeId, @ModelAttribute NoticePageVO pageVO) {
		noticeRepo.findById(noticeId).ifPresent(notice->model.addAttribute("pageVO", notice));
		return "notice/updatenotice";
	}
	@PostMapping("/update")
	public String modify(NoticePageVO pageVO, NoticeVO notice, RedirectAttributes attr) {
		noticeRepo.findById(notice.getNoticeId()).ifPresentOrElse(original->{
			original.setNoticeText(notice.getNoticeText());
			original.setNoticeTitle(notice.getNoticeTitle());
			NoticeVO updateNotice = noticeRepo.save(original);
			attr.addFlashAttribute("msg", updateNotice!=null?"update success":"update failed");
			attr.addFlashAttribute("pageVO", pageVO);
		}, ()->{System.out.println("수정할 데이터 없음");});
		
		return "redirect:/notice/list";
	}
	
	

	@PostMapping("/delete")
	public String delete(@RequestParam(value = "noticeId", required = false) Long noticeId) {
		noticeRepo.deleteById(noticeId);
		return "redirect:/notice/list";
	}
	
	
	
}

