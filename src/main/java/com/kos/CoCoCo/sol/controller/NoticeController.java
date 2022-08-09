package com.kos.CoCoCo.sol.controller;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.Principal;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ContentDisposition;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.amazonaws.auth.AWS3Signer;
import com.kos.CoCoCo.ja0.awsS3.AwsS3;
import com.kos.CoCoCo.ja0.repository.TeamRepository;
import com.kos.CoCoCo.ja0.repository.TeamUserRepositoryH;
import com.kos.CoCoCo.ja0.repository.UserRepositoryH;
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
@RequestMapping("/notice")
public class NoticeController {
	
	@Autowired
	TeamRepository tRepo;

	@Autowired
	TeamUserRepositoryH tuRepo;
	
	@Autowired
	UserRepositoryH uRepo;
	
	@Autowired
	AwsS3 aws;

	@Autowired
	NoticeService noticeService;
	
	@Autowired
	NoticeRepository noticeRepo;
	
	@Autowired
	NoticeFileRepository noticeFRepo;

	@GetMapping("")
	public String noticelist(NoticePageVO pageVO, NoticeVO notice, Model model, HttpSession session,
			HttpServletRequest requestPrinc, Principal principal) {
		System.out.println("principal.getName():" + principal.getName());
		UserVO user =uRepo.findById(principal.getName()).get();
		
		//TeamVO team = tRepo.findById(186L).get();
		//session.setAttribute("teamId", 186L);
	    Long teamId = (Long)session.getAttribute("teamId");
		TeamVO team = tRepo.findById(teamId).get();
		notice.setTeam(team);
		
		if(pageVO == null) {
			pageVO = new NoticePageVO();
		}
				
		Pageable page = pageVO.makePaging(0, new String[]{"fixedYN","noticeId"});
		Predicate prediacte = noticeRepo.makePredicate2(pageVO.getType(), pageVO.getKeyword(), pageVO.getKeyword2(), team);
		
		Page<NoticeVO> noticelist = noticeRepo.findAll(prediacte, page);
		PageMaker<NoticeVO> pgmaker = new PageMaker<>(noticelist);
		
		model.addAttribute("noticelist", pgmaker);
		model.addAttribute("pageVO", pageVO);
		
		return "notice/noticelist";
	}
	
	
	@GetMapping("/detail")
	public String detail(NoticePageVO pageVO, NoticeVO notice, Long noticeId, Model model) {
		
		log.info("공지번호 : " + noticeId);
		
		model.addAttribute("pageVO", pageVO);
		model.addAttribute("notice", noticeRepo.findById(noticeId).get());
		
		String folderPath="/static/noticefiles";
		File folder = new File(folderPath);
		File[] listOfFiles = folder.listFiles();
		model.addAttribute("nfiles",listOfFiles);
		
		List<NoticeFile> nflist = noticeFRepo.findByNotice(notice);
		System.out.println("nflist : " + nflist);
		model.addAttribute("nflist", nflist);
		
		
		return "notice/noticedetail";
	}
	
		
	
	@GetMapping("/insert")
	public String insert(UserVO user, HttpSession session, Principal principal) {
				
		return "notice/insertnotice";
	}
	
	@PostMapping("/insert")
	public String insertPost(HttpSession session, Model model, NoticeVO notice, MultipartFile[] files) throws Exception{
		System.out.println("공지vo"+notice);
				
		Long teamId = (Long) session.getAttribute("teamId");
		TeamVO team = tRepo.findById(teamId).get();
		notice.setTeam(team);
		
		UserVO writer = (UserVO) session.getAttribute("user");
		notice.setUser(writer);
				
		noticeService.insert(notice, files);
		
		return "redirect:/notice";
	}
	
	
	@GetMapping("/update")
	public String updateGet(NoticePageVO pageVO, NoticeFile noticefile, NoticeVO notice, Long noticeId, HttpSession session, Model model ) {
		model.addAttribute("notice", noticeRepo.findById(noticeId).get());
		model.addAttribute("noticefile", noticeFRepo.findByNotice(notice));
		List<NoticeFile> nflist = noticeFRepo.findByNotice(notice);
		model.addAttribute("nflist", nflist);
		return "notice/updatenotice";
	}
	
	@Transactional
	@PostMapping("/update")
	public String updatePost(NoticeVO notice, MultipartFile[] files, Model model) {
		noticeRepo.findById(notice.getNoticeId()).ifPresentOrElse(original->{
			
			original.setNoticeText(notice.getNoticeText());
			original.setNoticeTitle(notice.getNoticeTitle());
			original.setFixedYN(notice.getFixedYN());
			
			try {
				noticeService.insert(original, files);
			} catch (Exception e) {
				e.printStackTrace();
			}
			
		}, ()->{System.out.println("수정할 데이터 없음");});
		
				
		return "redirect:/notice";
	}
		
	@Transactional
	@GetMapping("/delete")
	public String delete(NoticePageVO pageVO, NoticeVO notice, NoticeFile file, RedirectAttributes attr) {
		
		System.out.println("NOTICE "+ notice);
		
		List<NoticeFile> files = noticeFRepo.findByNotice(notice);
		for(NoticeFile f:files) {
			aws.delete(f.getFilename());
		}
		noticeFRepo.deleteByNoticeId(notice.getNoticeId());
		noticeRepo.deleteById(notice.getNoticeId());
		
		return "redirect:/notice";
	}
	
	
	
	
	/*@Value("${spring.servlet.multipart.location}")
	String filePath;

	@GetMapping("/download")
	public ResponseEntity<Resource> download(@ModelAttribute NoticeFile file) throws IOException {
		
		Path path = Paths.get(filePath + "/" + file.getFileName());
		String contentType = Files.probeContentType(path);

		HttpHeaders headers = new HttpHeaders();
		headers.setContentDisposition(
		ContentDisposition.builder("attachment")
			.filename(file.getFileName(), StandardCharsets.UTF_8)
			.build());
		headers.add(HttpHeaders.CONTENT_TYPE, contentType);

		Resource resource = new InputStreamResource(Files.newInputStream(path));

		return new ResponseEntity<>(resource, headers, HttpStatus.OK);

	}*/
		

}

