package com.kos.CoCoCo.ja0.awsS3;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.DeleteObjectRequest;
import com.amazonaws.services.s3.model.PutObjectRequest;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Component
public class AwsS3 {

    private final AmazonS3Client amazonS3;

    @Value("${cloud.aws.s3.bucket}")
    private String bucket;

    public String upload(MultipartFile multipartFile, String dir) throws IOException {
        File uploadFile = convert(multipartFile).orElseThrow(() -> 
        						new IllegalArgumentException("MultipartFile -> File로 전환이 실패했습니다."));
        
        UUID uuid = UUID.randomUUID();
        String fileName = dir + uuid + "_" + uploadFile.getName();
        String uploadImageUrl = putS3(uploadFile, fileName);
        
        System.out.println("[upload file] " + uploadImageUrl);
        return uploadImageUrl;
    }

    public void delete(String targetFile){
    	if(targetFile == null) return;
    	
        amazonS3.deleteObject(bucket, targetFile);
        
        System.out.println("[delete file] " + targetFile);
    }
    
    private String putS3(File uploadFile, String fileName) {
        amazonS3.putObject(new PutObjectRequest(bucket, fileName, uploadFile).withCannedAcl(CannedAccessControlList.PublicRead));
        return amazonS3.getUrl(bucket, fileName).toString();
    }
    
    private Optional<File> convert(MultipartFile file) throws IOException {
        File convertFile = new File(file.getOriginalFilename());
        
        if(convertFile.createNewFile()) {
            try (FileOutputStream fos = new FileOutputStream(convertFile)) {
                fos.write(file.getBytes());
            }
            return Optional.of(convertFile);
        }
        return Optional.empty();
    }
}
