package com.example.arom1.common.util;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.*;
import com.example.arom1.common.exception.BaseException;
import com.example.arom1.common.response.BaseResponseStatus;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URLEncoder;
import java.util.Optional;
import java.util.UUID;


@Slf4j
@Component
public class AmazonS3Util {

    @Autowired
    private AmazonS3 amazonS3;

    @Value("${cloud.aws.s3.bucket}")
    private String bucket;

    private final String DIR_NAME = "images";

    public String uploadFile(MultipartFile file) throws IOException {
        //private 메서드 convert
        File uploadFile = convert(file)
                .orElseThrow(() -> new BaseException(BaseResponseStatus.FAIL_IMAGE_CONVERT));

        String uuid = UUID.randomUUID().toString();
        String filename = file.getOriginalFilename();
        String extension = filename.substring(filename.lastIndexOf("."));

       return putS3(uploadFile, DIR_NAME + "/" + uuid + extension);
    }
    private Optional<File> convert(MultipartFile file) throws IOException {
        log.info(file.getOriginalFilename());
        File convertFile = new File(file.getOriginalFilename());

        if(convertFile.createNewFile()) {
            try (FileOutputStream fos = new FileOutputStream(convertFile)) {
                fos.write(file.getBytes());
            }
            return Optional.of(convertFile);
        }
        return Optional.empty();
    }

    private String putS3(File uploadFile, String filename) {

        amazonS3.putObject(new PutObjectRequest(bucket, filename, uploadFile)
                        .withCannedAcl(CannedAccessControlList.PublicRead)); // PublicRead 권한

        // convert()함수로 인해서 로컬에 생성된 File 삭제 (MultipartFile -> File 전환 하며 로컬에 파일 생성됨)
        if(uploadFile.delete()) {
            log.info("파일이 삭제되었습니다.");
        }else {
            log.info("파일이 삭제되지 못했습니다.");
        }
        return amazonS3.getUrl(bucket, filename).toString();
    }

    //파일 삭제
    //파일 삭제시 키값은 모든 경로를 포함하여야 함...
    public void deleteFile(String key){
        amazonS3.deleteObject(new DeleteObjectRequest(bucket, DIR_NAME + "/" + key));
    }

    //URL 반환
    public String getUrl(String filename) {
        return amazonS3.getUrl(bucket, DIR_NAME + "/" + filename).toString();
    }


    //파일 다운로드
    public ResponseEntity<byte[]> download(String filename) throws IOException {
        S3Object awsS3Object = amazonS3.getObject(new GetObjectRequest(bucket, DIR_NAME + "/" + filename));
        S3ObjectInputStream s3is = awsS3Object.getObjectContent();
        byte[] bytes = s3is.readAllBytes();

        String downloadedFileName = URLEncoder.encode(filename, "UTF-8").replace("+", "%20");
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.IMAGE_JPEG);
        httpHeaders.setContentLength(bytes.length);
        httpHeaders.setContentDispositionFormData("attachment", downloadedFileName);
        return new ResponseEntity<>(bytes, httpHeaders, HttpStatus.OK);
    }

}
