package com.kos.CoCoCo.ja0.awsS3;


import java.io.ByteArrayInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.multipart.MultipartFile;

import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.AmazonS3Exception;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.services.s3.model.S3ObjectInputStream;
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
    
    public boolean download(String fileKey, String downloadFileName, HttpServletRequest request, HttpServletResponse response) {
        if (fileKey == null) {
            return false;
        }
        S3Object fullObject = null;
        try {
            fullObject = amazonS3.getObject(bucket, fileKey);
            if (fullObject == null) {
                return false;
            }
        } catch (AmazonS3Exception e) {
        	System.out.println("다운로드 파일이 존재하지 않습니다.");
        }

        OutputStream os = null;
        FileInputStream fis = null;
        boolean success = false;
        try {
            S3ObjectInputStream objectInputStream = fullObject.getObjectContent();
            byte[] bytes = IOUtils.toByteArray(objectInputStream);

            String fileName = null;
            if(downloadFileName != null) {
                //fileName= URLEncoder.encode(downloadFileName, "UTF-8").replaceAll("\\+", "%20");
                fileName=  getEncodedFilename(request, downloadFileName);
            } else {
                fileName=  getEncodedFilename(request, fileKey); // URLEncoder.encode(fileKey, "UTF-8").replaceAll("\\+", "%20");
            }

            response.setContentType("application/octet-stream;charset=UTF-8");
            response.setHeader("Content-Transfer-Encoding", "binary");
            response.setHeader( "Content-Disposition", "attachment; filename=\"" + fileName + "\";" );
            response.setHeader("Content-Length", String.valueOf(fullObject.getObjectMetadata().getContentLength()));
            response.setHeader("Set-Cookie", "fileDownload=true; path=/");
            FileCopyUtils.copy(bytes, response.getOutputStream());
            success = true;
        } catch (IOException e) {
        	System.out.println(e.getMessage());
        } finally {
            try {
                if (fis != null) {
                    fis.close();
                }
            } catch (IOException e) {
            	System.out.println(e.getMessage());
            }
            try {
                if (os != null) {
                    os.close();
                }
            } catch (IOException e) {
            	System.out.println(e.getMessage());
            }
        }
        return success;
    }
    
    private String getEncodedFilename(HttpServletRequest request, String displayFileName) throws UnsupportedEncodingException {
        String header = request.getHeader("User-Agent");

        String encodedFilename = null;
        if (header.indexOf("MSIE") > -1) {
            encodedFilename = URLEncoder.encode(displayFileName, "UTF-8").replaceAll("\\+", "%20");
        } else if (header.indexOf("Trident") > -1) {
            encodedFilename = URLEncoder.encode(displayFileName, "UTF-8").replaceAll("\\+", "%20");
        } else if (header.indexOf("Chrome") > -1) {
            StringBuffer sb = new StringBuffer();
            for (int i = 0; i < displayFileName.length(); i++) {
                char c = displayFileName.charAt(i);
                if (c > '~') {
                    sb.append(URLEncoder.encode("" + c, "UTF-8"));
                } else {
                    sb.append(c);
                }
            }
            encodedFilename = sb.toString();
        } else if (header.indexOf("Opera") > -1) {
            encodedFilename = "\"" + new String(displayFileName.getBytes("UTF-8"), "8859_1") + "\"";
        } else if (header.indexOf("Safari") > -1) {
            encodedFilename = URLDecoder.decode("\"" + new String(displayFileName.getBytes("UTF-8"), "8859_1") + "\"", "UTF-8");
        } else {
            encodedFilename = URLDecoder.decode("\"" + new String(displayFileName.getBytes("UTF-8"), "8859_1") + "\"", "UTF-8");
        }
        return encodedFilename;

    }
    
    public void rename(String sourceKey, String destinationKey){
    	amazonS3.copyObject(bucket, sourceKey, bucket, destinationKey);
    	amazonS3.deleteObject(bucket, sourceKey);
    }
}
