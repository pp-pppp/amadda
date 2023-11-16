package com.pppppp.amadda.global.service;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectResult;
import com.pppppp.amadda.IntegrationTestSupport;

import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;

class ImageServiceTest extends IntegrationTestSupport {

    @Autowired
    private ImageService imageService;
    @MockBean
    private AmazonS3 s3;

    @DisplayName("카카오의 프로필 사진을 다운로드해 S3에 저장한다. ")
    @Test
    void saveKakaoImgInS3() throws MalformedURLException {
        // given
        String url = "https://img5.yna.co.kr/etc/inner/KR/2023/10/13/AKR20231013148700005_07_i_P2.jpg";
        String fileName = "1111_"
            + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd-HH-mm-ss"))
            + ".jpg";
        String newUrl = "https://amadda-bucket.s3.ap-northeast-2.amazonaws.com/" + fileName;

        // when
        given(s3.getUrl(anyString(), anyString())).willReturn(new URL(newUrl));
        given(s3.putObject(anyString(), anyString(), any(InputStream.class), any(ObjectMetadata.class)))
            .willReturn(new PutObjectResult());

        // then
        imageService.saveKakaoImgInS3(url, fileName);
        verify(s3, times(1)).
            putObject(anyString(), anyString(), any(InputStream.class), any(ObjectMetadata.class));
    }

}
