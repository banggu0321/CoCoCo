package com.kos.CoCoCo.ja0.awsS3;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.util.IOUtils;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Component
public class AwsS3 {

    private final AmazonS3Client amazonS3;

    @Value("${cloud.aws.s3.bucket}")
    private String bucket;

    public String upload(MultipartFile file, String dir) throws IOException {
        String key =  dir + UUID.randomUUID() + "_" + file.getOriginalFilename();

        ObjectMetadata metadata = new ObjectMetadata();
        metadata.setContentType(file.getContentType());
        
        byte[] bytes = IOUtils.toByteArray(file.getInputStream());
        metadata.setContentLength(bytes.length);

        ByteArrayInputStream byteArrayIs = new ByteArrayInputStream(bytes);

        PutObjectRequest request = new PutObjectRequest(bucket, key, byteArrayIs, metadata);
        request.withCannedAcl(CannedAccessControlList.AuthenticatedRead); // 접근권한 체크
        
        amazonS3.putObject(request);
        
        System.out.println("[upload file] " + key);
        return amazonS3.getUrl(bucket, key).toString();
    }

    public void delete(String targetFile){
    	if(targetFile == null) return;
    	
    	String key = targetFile.substring(54);
//    	String encodeKey="";
//    	
//    	try {
//			encodeKey = URLDecoder.decode(key, "utf-8"); //한글 변환
//			amazonS3.deleteObject(bucket, encodeKey);
//		} catch (UnsupportedEncodingException e) {
//			e.printStackTrace();
//		}
		
    	amazonS3.deleteObject(bucket, key);
    	
        System.out.println("[delete file] " + key);
    }
}
