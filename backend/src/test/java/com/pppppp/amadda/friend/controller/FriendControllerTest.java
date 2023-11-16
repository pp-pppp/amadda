package com.pppppp.amadda.friend.controller;

import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.pppppp.amadda.ControllerTestSupport;
import com.pppppp.amadda.friend.dto.request.FriendRequestRequest;
import com.pppppp.amadda.friend.dto.request.GroupCreateRequest;
import com.pppppp.amadda.friend.dto.request.GroupUpdateRequest;
import com.pppppp.amadda.friend.dto.response.FriendRequestResponse;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;

class FriendControllerTest extends ControllerTestSupport {

    @DisplayName("친구 요청을 보낸다. ")
    @Test
    void sendFriendRequest() throws Exception {
        // given
        FriendRequestRequest request = FriendRequestRequest.builder()
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
        long requestSeq = 1L;

        // when // then
        mockMvc.perform(
                put(String.format("/api/friend/request/%d", requestSeq))
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
        long requestSeq = 1L;

        // stubbing
        given(friendRequestService.acceptFriendRequest(anyLong(), anyLong()))
            .willReturn(FriendRequestResponse.builder().ownerSeq(1L).friendSeq(2L).build());

        // when // then
        mockMvc.perform(
                post(String.format("/api/friend/request/%d", requestSeq))
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
        long friendUserSeq = 1L;

        // when // then
        mockMvc.perform(
                delete(String.format("/api/friend/%d", friendUserSeq))
                    .contentType(MediaType.APPLICATION_JSON)
            )
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.code").value("200"))
            .andExpect(jsonPath("$.status").value("OK"))
            .andExpect(jsonPath("$.message").value("OK"));
    }

    @DisplayName("그룹을 만든다. ")
    @Test
    void makeGroup() throws Exception {
        // given
        GroupCreateRequest request = GroupCreateRequest.builder()
            .groupName("그룹명1")
            .userSeqs(List.of(1234L, 2222L))
            .build();

        // when // then
        mockMvc.perform(
                post("/api/friend/group")
                    .content(objectMapper.writeValueAsString(request))
                    .contentType(MediaType.APPLICATION_JSON)
            )
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.code").value("200"))
            .andExpect(jsonPath("$.status").value("OK"))
            .andExpect(jsonPath("$.message").value("OK"));
    }

    @DisplayName("그룹을 만들때 선택된 유저가 없으면 예외처리. ")
    @Test
    void makeGroupWithoutUsers() throws Exception {
        // given
        GroupCreateRequest request = GroupCreateRequest.builder()
            .groupName("그룹명1")
            .userSeqs(List.of())
            .build();

        // when // then
        mockMvc.perform(
                post("/api/friend/group")
                    .content(objectMapper.writeValueAsString(request))
                    .contentType(MediaType.APPLICATION_JSON)
            )
            .andDo(print())
            .andExpect(status().isBadRequest());
    }

    @DisplayName("그룹을 만들때 그룹명이 없으면 예외처리. ")
    @Test
    void makeGroupWithoutName() throws Exception {
        // given
        GroupCreateRequest request = GroupCreateRequest.builder()
            .groupName("")
            .userSeqs(List.of(1234L, 2222L))
            .build();

        // when // then
        mockMvc.perform(
                post("/api/friend/group")
                    .content(objectMapper.writeValueAsString(request))
                    .contentType(MediaType.APPLICATION_JSON)
            )
            .andDo(print())
            .andExpect(status().isBadRequest());
    }

    @DisplayName("그룹의 이름과 멤버를 수정한다. ")
    @Test
    void editGroup() throws Exception {
        // given
        GroupUpdateRequest request = GroupUpdateRequest.builder()
            .groupSeq(0L)
            .groupName("그룹명1")
            .userSeqs(List.of(1234L, 2222L))
            .build();

        // when // then
        mockMvc.perform(
                put("/api/friend/group")
                    .content(objectMapper.writeValueAsString(request))
                    .contentType(MediaType.APPLICATION_JSON)
            )
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.code").value("200"))
            .andExpect(jsonPath("$.status").value("OK"))
            .andExpect(jsonPath("$.message").value("OK"));
    }

    @DisplayName("그룹을 수정할 때 선택된 유저가 없으면 예외처리. ")
    @Test
    void editGroupWithoutUsers() throws Exception {
        // given
        GroupUpdateRequest request = GroupUpdateRequest.builder()
            .groupSeq(0L)
            .groupName("그룹명1")
            .userSeqs(List.of())
            .build();

        // when // then
        mockMvc.perform(
                put("/api/friend/group")
                    .content(objectMapper.writeValueAsString(request))
                    .contentType(MediaType.APPLICATION_JSON)
            )
            .andDo(print())
            .andExpect(status().isBadRequest());
    }

    @DisplayName("그룹을 수정할 때 그룹명이 없으면 예외처리. ")
    @Test
    void editGroupWithoutName() throws Exception {
        // given
        GroupUpdateRequest request = GroupUpdateRequest.builder()
            .groupSeq(0L)
            .groupName(" ")
            .userSeqs(List.of(1234L, 2222L))
            .build();
        // when // then
        mockMvc.perform(
                put("/api/friend/group")
                    .content(objectMapper.writeValueAsString(request))
                    .contentType(MediaType.APPLICATION_JSON)
            )
            .andDo(print())
            .andExpect(status().isBadRequest());
    }

    @DisplayName("그룹을 수정할 때 그룹seq이 없으면 예외처리. ")
    @Test
    void editGroupWithoutGroupSeq() throws Exception {
        // given
        GroupUpdateRequest request = GroupUpdateRequest.builder()
            .groupName("aaa")
            .userSeqs(List.of(1234L, 2222L))
            .build();
        // when // then
        mockMvc.perform(
                put("/api/friend/group")
                    .content(objectMapper.writeValueAsString(request))
                    .contentType(MediaType.APPLICATION_JSON)
            )
            .andDo(print())
            .andExpect(status().isBadRequest());
    }

    @DisplayName("그룹을 삭제한다. ")
    @Test
    void deleteGroup() throws Exception {
        // given
        long groupSeq = 1L;

        // when // then
        mockMvc.perform(
                delete(String.format("/api/friend/group/%d", groupSeq))
                    .contentType(MediaType.APPLICATION_JSON)
            )
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.code").value("200"))
            .andExpect(jsonPath("$.status").value("OK"))
            .andExpect(jsonPath("$.message").value("OK"));
    }

    @DisplayName("친구를 검색한다. ")
    @Test
    void getFriendList() throws Exception {
        // given
        String key = "aaa";

        // when // then
        mockMvc.perform(
                get(String.format("/api/friend?searchKey=%s", key))
                    .contentType(MediaType.APPLICATION_JSON)
            )
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.code").value("200"))
            .andExpect(jsonPath("$.status").value("OK"))
            .andExpect(jsonPath("$.message").value("OK"));
    }

}
