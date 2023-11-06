package com.pppppp.amadda.schedule.service;

import com.pppppp.amadda.global.entity.exception.RestApiException;
import com.pppppp.amadda.global.entity.exception.errorcode.CategoryErrorCode;
import com.pppppp.amadda.global.entity.exception.errorcode.ScheduleErrorCode;
import com.pppppp.amadda.global.entity.exception.errorcode.UserErrorCode;
import com.pppppp.amadda.schedule.dto.request.CategoryCreateRequest;
import com.pppppp.amadda.schedule.dto.request.CommentCreateRequest;
import com.pppppp.amadda.schedule.dto.request.ScheduleCreateRequest;
import com.pppppp.amadda.schedule.dto.response.CategoryReadResponse;
import com.pppppp.amadda.schedule.dto.response.CommentReadResponse;
import com.pppppp.amadda.schedule.dto.response.ScheduleCreateResponse;
import com.pppppp.amadda.schedule.dto.response.ScheduleDetailReadResponse;
import com.pppppp.amadda.schedule.dto.response.ScheduleListReadResponse;
import com.pppppp.amadda.schedule.entity.Category;
import com.pppppp.amadda.schedule.entity.Comment;
import com.pppppp.amadda.schedule.entity.Participation;
import com.pppppp.amadda.schedule.entity.Schedule;
import com.pppppp.amadda.schedule.repository.CategoryRepository;
import com.pppppp.amadda.schedule.repository.CommentRepository;
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

    private final CommentRepository commentRepository;

    private final CategoryRepository categoryRepository;

    // TODO: 다중 조회 시 participation -> schedulelist 변환과정 리팩토링 필요. 메소드로 분리할 것

    @Transactional
    public ScheduleCreateResponse createSchedule(Long userSeq, ScheduleCreateRequest request) {

        // 유효한 사용자인지 확인
        User user = userRepository.findByUserSeq(userSeq)
            .orElseThrow(() -> new RestApiException(UserErrorCode.USER_NOT_FOUND));

        // request to entity
        Schedule newSchedule = scheduleRepository.save(request.toEntity(user));

        Long categorySeq = request.categorySeq();

        // 참가정보 바탕으로 참가자별 참석 정보 생성
        request.participants().forEach(response -> {
            // seq로 user 찾기, 없으면 예외 던짐
            User participant = userRepository.findById(response.userSeq())
                .orElseThrow(() -> new RestApiException(UserErrorCode.USER_NOT_FOUND));

            // 참석자가 생성자라면 카테고리 정보가 있는 경우 카테고리 정보 입력
            Participation p = Participation.builder()
                .scheduleName(request.scheduleName())
                .scheduleMemo(request.scheduleMemo())
                .alarmTime(request.alarmTime())
                .user(participant)
                .schedule(newSchedule)
                .category((participant.getUserSeq() == userSeq && categorySeq != null) ?
                    categoryRepository.findByCategorySeqAndIsDeletedFalse(categorySeq).orElseThrow(
                        () -> new RestApiException(CategoryErrorCode.CATEGORY_NOT_FOUND)) : null)
                .build();

            participationRepository.save(p);
        });

        // 생성한 사람의 일정의 개인 기록 가져오기
        Participation creatorParticipation = participationRepository.findBySchedule_ScheduleSeqAndUser_UserSeqAndIsDeletedFalse(
                newSchedule.getScheduleSeq(), userSeq)
            .orElseThrow(() -> new RestApiException(ScheduleErrorCode.SCHEDULE_NOT_FOUND));

        return ScheduleCreateResponse.of(newSchedule, request.participants(),
            creatorParticipation);
    }

    public ScheduleDetailReadResponse getScheduleDetail(Long scheduleSeq, Long userSeq) {
        Schedule schedule = scheduleRepository.findById(scheduleSeq)
            .orElseThrow(() -> new RestApiException(ScheduleErrorCode.SCHEDULE_NOT_FOUND));

        User user = userRepository.findByUserSeq(userSeq)
            .orElseThrow(() -> new RestApiException(UserErrorCode.USER_NOT_FOUND));

        List<UserReadResponse> participants = participationRepository.findBySchedule_ScheduleSeqAndIsDeletedFalse(
                scheduleSeq)
            .stream()
            .map(participation -> UserReadResponse.of(participation.getUser()))
            .collect(Collectors.toList());

        Participation participation = participationRepository.findBySchedule_ScheduleSeqAndUser_UserSeqAndIsDeletedFalse(
                scheduleSeq, user.getUserSeq())
            .orElseThrow(() -> new RestApiException(ScheduleErrorCode.SCHEDULE_NOT_FOUND));

        // 댓글 가져오기
        List<CommentReadResponse> comments = commentRepository.findBySchedule_ScheduleSeqAndIsDeletedFalse(
                scheduleSeq)
            .stream()
            .map(comment -> CommentReadResponse.of(comment, UserReadResponse.of(comment.getUser())))
            .collect(Collectors.toList());

        return ScheduleDetailReadResponse.of(schedule, participants, participation, comments);
    }

    public List<ScheduleListReadResponse> getScheduleList(Long userSeq) {
        User user = userRepository.findByUserSeq(userSeq)
            .orElseThrow(() -> new RestApiException(UserErrorCode.USER_NOT_FOUND));

        // 요청한 사용자의 참가 정보 모두 가져오기
        List<Participation> participations = participationRepository
            .findByUser_UserSeqAndIsDeletedFalse(user.getUserSeq());

        // 참가 정보를 바탕으로 일정 리스트 만들어서 반환
        return participations.stream()
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
    }

    public List<ScheduleListReadResponse> getScheduleByCategories(Long userSeq, String categories) {
        // 카테고리 seq 목록
        List<Long> seqs = List.of(categories.split(","))
            .stream()
            .map(Long::parseLong)
            .collect(Collectors.toList());

        // 해당하는 일정들 모아보기, 중복된 결과 피하기 위해 set으로 만듦
        List<ScheduleListReadResponse> schedules = new LinkedList<>();

        seqs.forEach(categorySeq -> {
            // 1. 해당 카테고리 seq가 유효한지 확인
            if (!categoryRepository.existsByUser_UserSeqAndCategorySeqAndIsDeletedFalse(userSeq,
                categorySeq)) {
                throw new RestApiException(CategoryErrorCode.CATEGORY_NOT_FOUND);
            }
            // 2. 카테고리에 포함된 애들 가져오기
            List<ScheduleListReadResponse> categorySchedules = participationRepository.findByUser_UserSeqAndCategory_CategorySeqAndIsDeletedFalse(
                    userSeq, categorySeq)
                .stream()
                .map(participation -> {
                    // 2-1. 참가하는 일정
                    Long scheduleSeq = participation.getSchedule().getScheduleSeq();
                    Schedule schedule = scheduleRepository.findByScheduleSeqAndIsDeletedFalse(
                            scheduleSeq)
                        .orElseThrow(() -> new RestApiException(
                            ScheduleErrorCode.SCHEDULE_NOT_FOUND));

                    // 2-2. 참가자 명단
                    List<UserReadResponse> participants = participationRepository
                        .findBySchedule_ScheduleSeqAndIsDeletedFalse(scheduleSeq)
                        .stream()
                        .map(p -> UserReadResponse.of(p.getUser()))
                        .collect(Collectors.toList());

                    // 2-3. 전체에게 권한이 있는 경우 authorizedUser 필드는 null로 만들어 response 반환
                    if (schedule.isAuthorizedAll()) {
                        return ScheduleListReadResponse.of(schedule, null, participants,
                            participation);
                    }
                    return ScheduleListReadResponse.of(schedule,
                        UserReadResponse.of(schedule.getUser()), participants, participation);
                })
                .collect(Collectors.toList());

            // 3. 목록에 추가
            schedules.addAll(categorySchedules);
        });

        // 전체 일정 목록 반환
        return schedules;
    }

    public List<ScheduleListReadResponse> getScheduleListByScheduleName(Long userSeq,
        String searchKey) {

        User user = userRepository.findByUserSeq(userSeq)
            .orElseThrow(() -> new RestApiException(UserErrorCode.USER_NOT_FOUND));

        // 요청한 사용자의 참가 정보 중에 searchKey를 포함하는 일정들 가져오고, 참가 정보를 바탕으로 일정 리스트 만들어서 반환
        return participationRepository.findByUser_UserSeqAndScheduleNameContainingAndIsDeletedFalse(
                userSeq, searchKey)
            .stream()
            .map(participation -> {
                // 1. 참가하는 일정
                Long scheduleSeq = participation.getSchedule().getScheduleSeq();
                Schedule schedule = scheduleRepository.findByScheduleSeqAndIsDeletedFalse(
                        scheduleSeq)
                    .orElseThrow(() -> new RestApiException(
                        ScheduleErrorCode.SCHEDULE_NOT_FOUND));

                // 2-2. 참가자 명단
                List<UserReadResponse> participants = participationRepository
                    .findBySchedule_ScheduleSeqAndIsDeletedFalse(scheduleSeq)
                    .stream()
                    .map(p -> UserReadResponse.of(p.getUser()))
                    .collect(Collectors.toList());

                // 2-3. 전체에게 권한이 있는 경우 authorizedUser 필드는 null로 만들어 response 반환
                if (schedule.isAuthorizedAll()) {
                    return ScheduleListReadResponse.of(schedule, null, participants,
                        participation);
                }
                return ScheduleListReadResponse.of(schedule,
                    UserReadResponse.of(schedule.getUser()), participants, participation);

            })
            .collect(Collectors.toList());
    }

    @Transactional
    public CommentReadResponse createCommentsOnSchedule(Long scheduleSeq,
        Long userSeq, CommentCreateRequest request) {
        Schedule schedule = scheduleRepository.findByScheduleSeqAndIsDeletedFalse(scheduleSeq)
            .orElseThrow(() -> new RestApiException(ScheduleErrorCode.SCHEDULE_NOT_FOUND));

        User user = userRepository.findByUserSeq(userSeq)
            .orElseThrow(() -> new RestApiException(UserErrorCode.USER_NOT_FOUND));

        Comment comment = commentRepository.save(request.toEntity(user, schedule));

        return CommentReadResponse.of(comment, UserReadResponse.of(user));
    }

    public CategoryReadResponse createCategory(Long userSeq, CategoryCreateRequest request) {

        User user = userRepository.findByUserSeq(userSeq)
            .orElseThrow(() -> new RestApiException(UserErrorCode.USER_NOT_FOUND));

        // 중복체크
        if (categoryRepository.existsByUser_UserSeqAndCategoryNameAndIsDeletedFalse(
            user.getUserSeq(), request.categoryName())) {
            throw new RestApiException(CategoryErrorCode.CATEGORY_ALREADY_EXISTS);
        }

        Category category = categoryRepository.save(request.toEntity(user));

        return CategoryReadResponse.of(category);
    }

    public List<CategoryReadResponse> getCategoryList(Long userSeq) {

        List<Category> categories = categoryRepository.findByUser_UserSeqAndIsDeletedFalse(
            userSeq);

        return categories.stream()
            .map(CategoryReadResponse::of)
            .collect(Collectors.toList());
    }
}
