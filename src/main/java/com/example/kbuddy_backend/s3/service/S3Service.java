package com.example.kbuddy_backend.s3.service;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.example.kbuddy_backend.s3.dto.response.S3Response;
import com.example.kbuddy_backend.s3.exception.ImageUploadException;
import java.io.IOException;
import java.util.UUID;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Slf4j
@RequiredArgsConstructor
@Service
public class S3Service {

    private final AmazonS3 amazonS3;

    @Getter
    @Value("${cloud.naver.s3.bucket}")
    private String bucketName;

    public S3Response saveFileWithUUID(MultipartFile multipartFile) {
        try {
            String originalFilename = multipartFile.getOriginalFilename();
            ObjectMetadata metadata = setMetadata(multipartFile);

            String uuid = UUID.randomUUID().toString();

            saveImageWithUUID(multipartFile, metadata, uuid);
            return S3Response.of(originalFilename, uuid, amazonS3.getUrl(bucketName, uuid).toString());
        } catch (IOException e) {
            log.error("s3에 file 저장중 에러 발생", e);
            throw new ImageUploadException(e.getMessage());
        }
    }

    private void saveImageWithUUID(MultipartFile multipartFile, ObjectMetadata metadata, String uuid) throws IOException {
        amazonS3.putObject(bucketName, uuid, multipartFile.getInputStream(), metadata);
    }

    public void deleteFile(String objectName) {
        amazonS3.deleteObject(bucketName, objectName);
    }

    private static ObjectMetadata setMetadata(MultipartFile multipartFile) {
        ObjectMetadata metadata = new ObjectMetadata();
        metadata.setContentLength(multipartFile.getSize());
        metadata.setContentType(multipartFile.getContentType());
        return metadata;
    }
}
