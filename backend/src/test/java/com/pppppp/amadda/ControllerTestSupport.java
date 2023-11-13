package com.pppppp.amadda;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.pppppp.amadda.alarm.controller.AlarmController;
import com.pppppp.amadda.alarm.service.AlarmService;
import com.pppppp.amadda.alarm.service.KafkaProducer;
import com.pppppp.amadda.friend.controller.FriendController;
import com.pppppp.amadda.friend.service.FriendRequestService;
import com.pppppp.amadda.friend.service.FriendService;
import com.pppppp.amadda.friend.service.GroupMemberService;
import com.pppppp.amadda.friend.service.UserGroupService;
import com.pppppp.amadda.global.util.TokenProvider;
import com.pppppp.amadda.schedule.controller.ScheduleController;
import com.pppppp.amadda.schedule.service.ScheduleService;
import com.pppppp.amadda.user.controller.UserController;
import com.pppppp.amadda.user.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(controllers = {
    AlarmController.class,
    FriendController.class,
    ScheduleController.class,
    UserController.class
})
public abstract class ControllerTestSupport {

    @Autowired
    protected MockMvc mockMvc;

    @Autowired
    protected ObjectMapper objectMapper;

    @MockBean
    protected TokenProvider tokenProvider;

    @MockBean
    protected KafkaProducer kafkaProducer;

    @MockBean
    protected AlarmService alarmService;

    @MockBean
    protected FriendService friendService;

    @MockBean
    protected FriendRequestService friendRequestService;

    @MockBean
    protected UserGroupService userGroupService;

    @MockBean
    protected GroupMemberService groupMemberService;

    @MockBean
    protected ScheduleService scheduleService;

    @MockBean
    protected UserService userService;
}
