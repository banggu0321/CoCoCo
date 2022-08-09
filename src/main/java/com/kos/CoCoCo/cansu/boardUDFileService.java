package com.kos.CoCoCo.cansu;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class boardUDFileService {
	
	public List<String> uploadFile(MultipartFile[] files) throws IllegalStateException, IOException {
		
		String filePath = System.getProperty("user.dir") + "\\src\\main\\resources\\static\\boardFiles";
//		UUID uuid = UUID.randomUUID();
		
		System.out.println("file path: "+filePath);
//		System.out.println("uuid: "+uuid);
		
		List<String> fileNames = new ArrayList<>();
		for(MultipartFile file: files) {
			String name = file.getOriginalFilename();
			
			if(name==null || name.equals("")) { 
				continue;
			}
//			String fileName = uuid + "_" + file.getOriginalFilename();
			
			String fileName = file.getOriginalFilename();
			fileNames.add(fileName);
			
			File saveFile = new File(filePath, fileName);
			file.transferTo(saveFile);
		}
		return fileNames;
	}

}
