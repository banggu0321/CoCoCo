package com.kos.CoCoCo.cansu;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class boardUDFileService {
	
	public void uploadFile(MultipartFile[] files) throws IllegalStateException, IOException {
		
		String filePath = System.getProperty("user.dir") + "\\src\\main\\resources\\static\\boardFiles";
		UUID uuid = UUID.randomUUID();
		
		System.out.println("file path: "+filePath);
		System.out.println("uuid: "+uuid);
		
		for(MultipartFile file: files) {
			String name = file.getOriginalFilename();
			
			if(name==null || name.equals("")) { 
				continue;
			}
			String fileName = uuid + "_" + file.getOriginalFilename();
			File saveFile = new File(filePath, fileName);
			file.transferTo(saveFile);
		}
	}

}
