package com.pppppp.amadda.schedule.service;

import com.pppppp.amadda.global.entity.exception.RestApiException;
import com.pppppp.amadda.global.entity.exception.errorcode.ScheduleErrorCode;
import com.pppppp.amadda.global.entity.exception.errorcode.UserErrorCode;
import com.pppppp.amadda.schedule.dto.request.ScheduleCreateRequest;
import com.pppppp.amadda.schedule.dto.response.ScheduleCreateResponse;
import com.pppppp.amadda.schedule.dto.response.ScheduleDetailReadResponse;
import com.pppppp.amadda.schedule.dto.response.ScheduleListReadResponse;
import com.pppppp.amadda.schedule.entity.Participation;
import com.pppppp.amadda.schedule.entity.Schedule;
import com.pppppp.amadda.schedule.repository.ParticipationRepository;
import com.pppppp.amadda.schedule.repository.ScheduleRepository;
import com.pppppp.amadda.user.dto.response.UserReadResponse;
import com.pppppp.amadda.user.entity.User;
import com.pppppp.amadda.user.repository.UserRepository;
import jakarta.transaction.Transactional;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class ScheduleService {

    private final ScheduleRepository scheduleRepository;

    private final ParticipationRepository participationRepository;

    private final UserRepository userRepository;

    @Transactional
    public ScheduleCreateResponse createSchedule(User user, ScheduleCreateRequest request) {

        Schedule newSchedule = request.toEntity(user);

        scheduleRepository.save(newSchedule);

        List<Participation> newParticipations = new LinkedList<>();

        request.participants().stream().forEach(response -> {
            // seq로 user 찾기, 없으면 예외 던짐
            User participant = userRepository.findById(response.userSeq())
                .orElseThrow(() -> new RestApiException(UserErrorCode.USER_NOT_FOUND));

            Participation newParticipation = Participation.builder()
                .scheduleName(request.scheduleName())
                .scheduleMemo(request.scheduleMemo())
                .alarmTime(request.alarmTime())
                .user(participant)
                .schedule(newSchedule)
                .build();

            newParticipations.add(newParticipation);
        });

        participationRepository.saveAll(newParticipations);

        // 초기 생성 정보 가져오기
        Participation creatorParticipation = newParticipations.get(0);

        return ScheduleCreateResponse.of(newSchedule, request.participants(), creatorParticipation);
    }

    public ScheduleDetailReadResponse getScheduleDetail(Long scheduleSeq, User user) {
        Schedule schedule = scheduleRepository.findById(scheduleSeq)
            .orElseThrow(() -> new RestApiException(ScheduleErrorCode.SCHEDULE_NOT_FOUND));

        List<UserReadResponse> participants = participationRepository.findBySchedule_ScheduleSeqAndIsDeletedFalse(
                scheduleSeq)
            .stream()
            .map(participation -> UserReadResponse.of(participation.getUser()))
            .collect(Collectors.toList());

        Participation participation = participationRepository.findBySchedule_ScheduleSeqAndUser_UserSeqAndIsDeletedFalse(
                scheduleSeq, user.getUserSeq())
            .orElseThrow(() -> new RestApiException(ScheduleErrorCode.SCHEDULE_NOT_FOUND));

        return ScheduleDetailReadResponse.of(schedule, participants, participation);
    }

    public List<ScheduleListReadResponse> getScheduleList(User user) {
        // 요청한 사용자의 참가 정보 모두 가져오기
        List<Participation> participations = participationRepository
            .findByUser_UserSeqAndIsDeletedFalse(user.getUserSeq());

        // 참가 정보를 바탕으로 일정 리스트 가져오기
        List<ScheduleListReadResponse> schedules = participations.stream()
            .map(participation -> {

                // 1. 참가하는 일정
                Long scheduleSeq = participation.getSchedule().getScheduleSeq();
                Schedule schedule = scheduleRepository.findByScheduleSeqAndIsDeletedFalse(
                        scheduleSeq)
                    .orElseThrow(() -> new RestApiException(ScheduleErrorCode.SCHEDULE_NOT_FOUND));

                // 2. 참가자 명단
                List<UserReadResponse> participants = participationRepository
                    .findBySchedule_ScheduleSeqAndIsDeletedFalse(scheduleSeq)
                    .stream()
                    .map(p -> UserReadResponse.of(p.getUser()))
                    .collect(Collectors.toList());

                // 3. 전체에게 권한이 있는 경우 authorizedUser 필드는 null로 만들어 response 반환
                if (schedule.isAuthorizedAll()) {
                    return ScheduleListReadResponse.of(schedule, null, participants,
                        participation);
                }
                return ScheduleListReadResponse.of(schedule,
                    UserReadResponse.of(schedule.getUser()), participants, participation);
            })
            .collect(Collectors.toList());

        return schedules;
    }
}
