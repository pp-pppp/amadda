package com.pppppp.amadda.friend.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.pppppp.amadda.alarm.service.AlarmService;
import com.pppppp.amadda.friend.dto.request.FriendRequestRequest;
import com.pppppp.amadda.friend.dto.request.GroupCreateRequest;
import com.pppppp.amadda.friend.dto.request.GroupPatchRequest;
import com.pppppp.amadda.friend.dto.response.FriendRequestResponse;
import com.pppppp.amadda.friend.service.FriendRequestService;
import com.pppppp.amadda.friend.service.GroupMemberService;
import com.pppppp.amadda.friend.service.UserGroupService;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(controllers = FriendController.class)
class FriendControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private FriendRequestService friendRequestService;
    @MockBean
    private UserGroupService userGroupService;
    @MockBean
    private GroupMemberService groupMemberService;
    @MockBean
    private AlarmService alarmService;

    @DisplayName("친구 요청을 보낸다. ")
    @Test
    void sendFriendRequest() throws Exception {
        // given
        FriendRequestRequest request = FriendRequestRequest.builder()
                .userSeq(1111L)
                .targetSeq(1234L)
                .build();

        // stubbing
        when(friendRequestService.createFriendRequest(any()))
            .thenReturn(FriendRequestResponse.builder().ownerSeq(request.userSeq())
                .friendSeq(request.targetSeq()).build());

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

        // stubbing
        when(friendRequestService.acceptFriendRequest(anyLong(), anyLong()))
            .thenReturn(FriendRequestResponse.builder().ownerSeq(1L).friendSeq(2L).build());

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

    @DisplayName("그룹을 만든다. ")
    @Test
    void makeGroup() throws Exception {
        // given
        GroupCreateRequest request = GroupCreateRequest.builder()
                .ownerSeq(1111L)
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
                .ownerSeq(1111L)
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
                .ownerSeq(1111L)
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
        GroupPatchRequest request = GroupPatchRequest.builder()
                .groupSeq(0L)
                .groupName("그룹명1")
                .userSeqs(List.of(1234L, 2222L))
                .build();

        // when // then
        mockMvc.perform(
                        patch("/api/friend/group")
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
        GroupPatchRequest request = GroupPatchRequest.builder()
                .groupSeq(0L)
                .groupName("그룹명1")
                .userSeqs(List.of())
                .build();

        // when // then
        mockMvc.perform(
                        patch("/api/friend/group")
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
        GroupPatchRequest request = GroupPatchRequest.builder()
                .groupSeq(0L)
                .groupName(" ")
                .userSeqs(List.of(1234L, 2222L))
                .build();
        // when // then
        mockMvc.perform(
                        patch("/api/friend/group")
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
        GroupPatchRequest request = GroupPatchRequest.builder()
                .groupName("aaa")
                .userSeqs(List.of(1234L, 2222L))
                .build();
        // when // then
        mockMvc.perform(
                        patch("/api/friend/group")
                                .content(objectMapper.writeValueAsString(request))
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

}
