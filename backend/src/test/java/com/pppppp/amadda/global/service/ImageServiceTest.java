package com.pppppp.amadda.global.service;

import static org.mockito.BDDMockito.given;

import com.pppppp.amadda.IntegrationTestSupport;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.mock.mockito.MockBean;

class ImageServiceTest extends IntegrationTestSupport {

    @MockBean
    private ImageService imageService;

    @DisplayName("카카오의 프로필 사진을 다운로드해 S3에 저장한다. ")
    @Test
    void saveKakaoImgInS3() {
        // given
        String url = "https://img5.yna.co.kr/etc/inner/KR/2023/10/13/AKR20231013148700005_07_i_P2.jpg";
        String fileName = "1111_"
            + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd-HH-mm-ss"))
            + ".jpg";
        ;

        // when // then
        given(imageService.saveKakaoImgInS3(url, fileName))
            .willReturn("https://amadda-bucket.s3.ap-northeast-2.amazonaws.com/" + fileName);
    }

}
