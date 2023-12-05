package com.shopping2.item.service;


import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.services.s3.model.PutObjectResult;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AwsS3Service {

    private final AmazonS3 amazonS3;

    @Value("${cloud.aws.s3.bucket}")
    private String bucketName; //버킷 이름

    private String changedImageName(String originName) { //이미지 이름 중복 방지를 위해 랜덤으로 생성
        String random = UUID.randomUUID().toString();
        return random + originName;
    }

    public String uploadImageToS3(MultipartFile image) { // 이미지를 S3에 업로드하고 이미지의 url을 반환
        String originName = image.getOriginalFilename(); // 원본 이미지 이름
        String ext = originName.substring(originName.lastIndexOf(".")); // 확장자
        String changedName = changedImageName(originName); // 새로 생성된 이미지 이름

        ObjectMetadata metadata = new ObjectMetadata();
        metadata.setContentType("image/" + ext);
        try {
            PutObjectResult putObjectResult = amazonS3.putObject(new PutObjectRequest(
                    bucketName, changedName, image.getInputStream(), metadata
            ).withCannedAcl(CannedAccessControlList.PublicRead));

        } catch (IOException e) {
            throw new IllegalArgumentException(); // 예외 던짐.
        }
        return amazonS3.getUrl(bucketName, changedName).toString(); //데이터베이스에 저장할 이미지가 저장된 주소
    }

    public void deleteImage(String fileName) {
        amazonS3.deleteObject(bucketName, fileName);
    }
}
