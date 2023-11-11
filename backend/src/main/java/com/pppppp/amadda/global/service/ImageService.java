package com.pppppp.amadda.global.service;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.pppppp.amadda.global.entity.exception.RestApiException;
import com.pppppp.amadda.global.entity.exception.errorcode.ImageErrorCode;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ImageService {

    private final AmazonS3 s3;

    @Value("${cloud.aws.s3.bucket}")
    private String bucket;

    public String saveKakaoImgInS3(String imgUrl, String fileName) {
        byte[] data = downloadUrlImg(imgUrl);
        return uploadImgInS3(data, fileName);
    }

    private byte[] downloadUrlImg(String imgUrl) {
        try (InputStream in = new URL(imgUrl).openStream()) {
            return in.readAllBytes();
        } catch (IOException e) {
            throw new RestApiException(ImageErrorCode.IMAGE_DOWNLOAD_FAILED);
        }
    }

    private String uploadImgInS3(byte[] data, String name) {
        try (InputStream inputStream = new ByteArrayInputStream(data)) {
            ObjectMetadata metadata = new ObjectMetadata();
            metadata.setContentType("image/jpg");
            s3.putObject(bucket, name, inputStream, metadata);
            return s3.getUrl(bucket, name).toString();
        } catch (IOException e) {
            throw new RestApiException(ImageErrorCode.IMAGE_UPLOAD_FAILED);
        }
    }

}
