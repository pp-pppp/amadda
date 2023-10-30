package com.pppppp.amadda.friend.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.pppppp.amadda.friend.dto.request.FriendRequestRequest;
import com.pppppp.amadda.friend.service.FriendRequestService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = FriendController.class)
class FriendControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private FriendRequestService friendRequestService;

    @DisplayName("친구 요청을 보낸다. ")
    @Test
    void sendFriendRequest() throws Exception {
        // given
        FriendRequestRequest request = FriendRequestRequest.builder()
                .userSeq(1111L)
                .targetSeq(1234L)
                .build();

        // when // then
        mockMvc.perform(
                post("/api/friend/request")
                        .content(objectMapper.writeValueAsString(request))
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value("200"))
                .andExpect(jsonPath("$.status").value("OK"))
                .andExpect(jsonPath("$.message").value("OK"));
    }

    @DisplayName("대상 유저 없이 요청을 보낸 경우 예외가 발생한다. ")
    @Test
    void sendFriendRequestWithOutTarget() throws Exception {
        // given
        FriendRequestRequest request = FriendRequestRequest.builder()
                .userSeq(1111L)
                .build();

        // when // then
        mockMvc.perform(
                        post("/api/friend/request")
                                .content(objectMapper.writeValueAsString(request))
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @DisplayName("유저(본인) 없이 요청을 보낸 경우 예외가 발생한다. ")
    @Test
    void sendFriendRequestWithOutUser() throws Exception {
        // given
        FriendRequestRequest request = FriendRequestRequest.builder()
                .targetSeq(1111L)
                .build();

        // when // then
        mockMvc.perform(
                        post("/api/friend/request")
                                .content(objectMapper.writeValueAsString(request))
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andDo(print())
                .andExpect(status().isBadRequest());
    }


    @DisplayName("받은 친구 요청을 거절한다. ")
    @Test
    void declineFriendRequest() throws Exception {
        // given
        Long requestSeq = 1L;

        // when // then
        mockMvc.perform(
                        patch("/api/friend/request/"+requestSeq)
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value("200"))
                .andExpect(jsonPath("$.status").value("OK"))
                .andExpect(jsonPath("$.message").value("OK"));
    }

    @DisplayName("받은 친구 요청을 수락한다. ")
    @Test
    void acceptFriendRequest() throws Exception {
        // given
        Long requestSeq = 1L;

        // when // then
        mockMvc.perform(
                post("/api/friend/request/"+requestSeq)
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value("200"))
                .andExpect(jsonPath("$.status").value("OK"))
                .andExpect(jsonPath("$.message").value("OK"));
    }

    @DisplayName("친구를 삭제한다.")
    @Test
    void deleteFriend() throws Exception {
        // given
        Long friendUserSeq = 1L;

        // when // then
        mockMvc.perform(
                        delete("/api/friend/"+friendUserSeq)
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value("200"))
                .andExpect(jsonPath("$.status").value("OK"))
                .andExpect(jsonPath("$.message").value("OK"));
    }

}