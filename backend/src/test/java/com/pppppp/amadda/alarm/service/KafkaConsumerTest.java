package com.pppppp.amadda.alarm.service;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import com.pppppp.amadda.IntegrationTestSupport;
import com.pppppp.amadda.alarm.dto.topic.alarm.AlarmFriendAccept;
import com.pppppp.amadda.alarm.dto.topic.alarm.AlarmFriendRequest;
import com.pppppp.amadda.alarm.entity.KafkaTopic;
import com.pppppp.amadda.alarm.repository.AlarmRepository;
import com.pppppp.amadda.friend.entity.FriendRequest;
import com.pppppp.amadda.friend.repository.FriendRequestRepository;
import com.pppppp.amadda.user.entity.User;
import com.pppppp.amadda.user.repository.UserRepository;
import java.io.IOException;
import java.util.List;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.test.context.EmbeddedKafka;

@EmbeddedKafka(partitions = 1, brokerProperties = {"listeners=PLAINTEXT://localhost:9092",
    "port=9092"})
class KafkaConsumerTest extends IntegrationTestSupport {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private FriendRequestRepository friendRequestRepository;

    private KafkaConsumer kafkaConsumer;
    private AlarmRepository alarmRepository;

    @BeforeEach
    void setUp() {
        alarmRepository = mock(AlarmRepository.class);
        kafkaConsumer = new KafkaConsumer(alarmRepository, userRepository);

    }

    @AfterEach
    void tearDown() {
        alarmRepository.deleteAllInBatch();
        friendRequestRepository.deleteAllInBatch();
        userRepository.deleteAllInBatch();
    }

    @DisplayName("alarm.friend-request consume 검증")
    @Test
    public void alarm_friend_request() throws IOException {
        // given
        User u1 = User.create(1111L, "유저1", "id1", "imageUrl1");
        User u2 = User.create(1234L, "유저2", "id2", "imageUrl2");
        List<User> users = userRepository.saveAll(List.of(u1, u2));
        User owner = users.get(0);
        User friend = users.get(1);

        FriendRequest friendRequest = FriendRequest.create(owner, friend);
        FriendRequest savedFriendRequest = friendRequestRepository.save(friendRequest);

        String topic = KafkaTopic.ALARM_FRIEND_REQUEST;
        String key = String.valueOf(friend.getUserSeq());
        AlarmFriendRequest value = AlarmFriendRequest.create(
            savedFriendRequest.getRequestSeq(), friend.getUserSeq(),
            friend.getUserName());
        ConsumerRecord<String, AlarmFriendRequest> consumerRecord = new ConsumerRecord<>(topic, 0,
            0, key, value);

        // when
        kafkaConsumer.consumeFriendRequest(consumerRecord);

        // then
        verify(alarmRepository, times(1)).save(any());

    }

    @DisplayName("alarm.friend-accept consume 검증")
    @Test
    public void alarm_friend_accept() throws IOException {
        // given
        User u1 = User.create(1111L, "유저1", "id1", "imageUrl1");
        User u2 = User.create(1234L, "유저2", "id2", "imageUrl2");
        List<User> users = userRepository.saveAll(List.of(u1, u2));
        User owner = users.get(0);
        User friend = users.get(1);

        FriendRequest friendRequest = FriendRequest.create(owner, friend);
        FriendRequest savedFriendRequest = friendRequestRepository.save(friendRequest);

        String topic = KafkaTopic.ALARM_FRIEND_ACCEPT;
        String key = String.valueOf(owner.getUserSeq());
        AlarmFriendAccept value = AlarmFriendAccept.create(friend.getUserSeq(),
            friend.getUserName());
        ConsumerRecord<String, AlarmFriendAccept> consumerRecord = new ConsumerRecord<>(topic, 0,
            0, key, value);

        // when
        kafkaConsumer.consumeFriendAccept(consumerRecord);

        // then
        verify(alarmRepository, times(1)).save(any());

    }
}
