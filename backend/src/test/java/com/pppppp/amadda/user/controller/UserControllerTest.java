package com.pppppp.amadda.user.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.pppppp.amadda.ControllerTestSupport;
import com.pppppp.amadda.user.dto.request.UserIdCheckRequest;
import com.pppppp.amadda.user.dto.request.UserInitRequest;
import com.pppppp.amadda.user.dto.request.UserJwtRequest;
import com.pppppp.amadda.user.dto.request.UserNameCheckRequest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;

class UserControllerTest extends ControllerTestSupport {

    @DisplayName("로그인 후 3개의 토큰들을 받는다. ")
    @Test
    void getTokens() throws Exception {
        // given
        UserJwtRequest request = UserJwtRequest.builder()
            .kakaoId("1111")
            .imageUrl("imgUrl1")
            .build();

        // when // then
        mockMvc.perform(
                post("/api/user/login")
                    .content(objectMapper.writeValueAsString(request))
                    .contentType(MediaType.APPLICATION_JSON)
            )
            .andDo(print())
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.code").value("200"))
            .andExpect(jsonPath("$.status").value("OK"))
            .andExpect(jsonPath("$.message").value("OK"));
    }

    @DisplayName("사용자를 회원가입 처리한다. ")
    @Test
    void signupUser() throws Exception {
        // given
        UserInitRequest request = UserInitRequest.builder()
            .kakaoId("1111")
            .imageUrl("imgUrl")
            .userId("aaa")
            .userName("sss")
            .build();

        // when // then
        mockMvc.perform(
                post("/api/user/signup")
                    .content(objectMapper.writeValueAsString(request))
                    .contentType(MediaType.APPLICATION_JSON)
            )
            .andDo(print())
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.code").value("200"))
            .andExpect(jsonPath("$.status").value("OK"))
            .andExpect(jsonPath("$.message").value("OK"));
    }

    @DisplayName("토큰의 유효성을 판단한다. ")
    @Test
    void validateAccessToken() throws Exception {
        // given
        // when // then
        mockMvc.perform(
                get("/api/user/access")
                    .contentType(MediaType.APPLICATION_JSON)
            )
            .andDo(print())
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.code").value("200"))
            .andExpect(jsonPath("$.status").value("OK"))
            .andExpect(jsonPath("$.message").value("OK"));
    }

    @DisplayName("검색키에 해당하는 유저와 본인과 친구여부를 조회한다. ")
    @Test
    void getUserInfoAndRelation() throws Exception {
        // given
        String key = "검색키";

        // when // then
        mockMvc.perform(
                get(String.format("/api/user?search-key=%s", key))
                    .contentType(MediaType.APPLICATION_JSON)
            )
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.code").value("200"))
            .andExpect(jsonPath("$.status").value("OK"))
            .andExpect(jsonPath("$.message").value("OK"));
    }

    @DisplayName("내 정보를 조회한다. ")
    @Test
    void getMyUserInfo() throws Exception {
        // given

        // when // then
        mockMvc.perform(
                get("/api/user/my")
                    .contentType(MediaType.APPLICATION_JSON)
            )
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.code").value("200"))
            .andExpect(jsonPath("$.status").value("OK"))
            .andExpect(jsonPath("$.message").value("OK"));
    }

    @DisplayName("아이디 중복/유효 여부를 반환한다. ")
    @Test
    void checkId() throws Exception {
        // given
        UserIdCheckRequest request = UserIdCheckRequest.builder()
            .userId("iddd")
            .build();

        // when // then
        mockMvc.perform(
                post("/api/user/check/id")
                    .content(objectMapper.writeValueAsString(request))
                    .contentType(MediaType.APPLICATION_JSON)
            )
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.code").value("200"))
            .andExpect(jsonPath("$.status").value("OK"))
            .andExpect(jsonPath("$.message").value("OK"));
    }

    @DisplayName("닉네임 유효 여부를 반환한다. ")
    @Test
    void checkName() throws Exception {
        // given
        UserNameCheckRequest request = UserNameCheckRequest.builder()
            .userName("nameee")
            .build();

        // when // then
        mockMvc.perform(
                post("/api/user/check/name")
                    .content(objectMapper.writeValueAsString(request))
                    .contentType(MediaType.APPLICATION_JSON)
            )
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.code").value("200"))
            .andExpect(jsonPath("$.status").value("OK"))
            .andExpect(jsonPath("$.message").value("OK"));
    }

    @DisplayName("유저seq로 호버한 유저의 정보와 나와의 친구관계를 표시한다. ")
    @Test
    void getUserInfoForHover() throws Exception {
        // given
        long targetSeq = 0L;

        // when // then
        mockMvc.perform(
                get(String.format("/api/user/%d", targetSeq))
                    .contentType(MediaType.APPLICATION_JSON)
            )
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.code").value("200"))
            .andExpect(jsonPath("$.status").value("OK"))
            .andExpect(jsonPath("$.message").value("OK"));
    }

    @DisplayName("유저를 회원탈퇴 처리한다. ")
    @Test
    void deleteUser() throws Exception {
        // given // when // then
        mockMvc.perform(
                delete("/api/user")
                    .contentType(MediaType.APPLICATION_JSON)
            )
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.code").value("200"))
            .andExpect(jsonPath("$.status").value("OK"))
            .andExpect(jsonPath("$.message").value("OK"));
    }

}
