package com.kos.CoCoCo.sol.service;

import java.io.File;
import java.util.List;
import java.util.UUID;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.kos.CoCoCo.sol.repository.NoticeFileRepository;
import com.kos.CoCoCo.sol.repository.NoticeRepository;
import com.kos.CoCoCo.sol.vo.NoticeDTO;
import com.kos.CoCoCo.sol.vo.NoticeFile;
import com.kos.CoCoCo.vo.NoticeVO;

@Service
public class NoticeService {

	@Autowired
	private NoticeRepository noticeRepo;
	
	@Autowired
	private NoticeFileRepository noticeFRepo;
		
	public void insert(NoticeVO notice, MultipartFile[] files) throws Exception {
				
		NoticeVO newNotice = noticeRepo.save(notice);
		String filePath = System.getProperty("user.dir") + "\\src\\main\\resources\\static\\noticefiles";
		UUID uuid = UUID.randomUUID();
		for(MultipartFile file:files) {
			String fname = file.getOriginalFilename();
			if(fname==null || fname.equals("")) { 
				continue;
			}
			String fileName = uuid + "_" + file.getOriginalFilename();
			File saveFile = new File(filePath, fileName);
			file.transferTo(saveFile); //upload 
			
			NoticeFile noticefile = NoticeFile.builder()
					.filename(fileName)
					.originFname(file.getOriginalFilename())
					.notice(newNotice)
					.build();
			
			noticeFRepo.save(noticefile);
		}
		
	}
		
}

