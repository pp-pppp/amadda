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
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Objects;
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


    @Transactional
    public ScheduleCreateResponse createSchedule(Long userSeq, ScheduleCreateRequest request) {

        // 사용자 유효 체크
        User user = findUserInfo(userSeq);

        // request to entity
        Schedule newSchedule = scheduleRepository.save(request.toEntity(user));

        // 참가정보 바탕으로 참가자별 참석 정보 생성
        createParticipation(userSeq, request, newSchedule);

        // 생성한 사람의 일정의 개인 기록 가져오기
        Participation creatorParticipation = findParticipationInfo(newSchedule.getScheduleSeq(),
            userSeq);

        return ScheduleCreateResponse.of(newSchedule, request.participants(),
            creatorParticipation);
    }

    public ScheduleDetailReadResponse getScheduleDetail(Long scheduleSeq, Long userSeq) {

        // 사용자, 일정 유효 체크
        findUserInfo(userSeq);
        Schedule schedule = findScheduleInfo(scheduleSeq);

        // 명단 가져오기
        List<UserReadResponse> participants = getParticipatingUserList(scheduleSeq);

        // 일정 생성
        Participation participation = findParticipationInfo(scheduleSeq, userSeq);

        // 댓글 가져오기
        List<CommentReadResponse> comments = findCommentListBySchedule(scheduleSeq);

        return ScheduleDetailReadResponse.of(schedule, participants, participation, comments);
    }

    public List<UserReadResponse> getParticipatingUserList(Long scheduleSeq) {
        return participationRepository.findBySchedule_ScheduleSeqAndIsDeletedFalse(
                scheduleSeq)
            .stream()
            .map(participation -> UserReadResponse.of(participation.getUser()))
            .toList();
    }

    public List<ScheduleListReadResponse> getScheduleListBySearchCondition(Long userSeq,
        Map<String, String> searchCondition) {
        return findScheduleListBySearchCondition(userSeq, searchCondition);
    }

    public List<UserReadResponse> getParticipatingUserListBySearchKey(Long scheduleSeq,
        String searchKey) {

        return findParticipatorListByUserName(scheduleSeq, searchKey);
    }

    public CommentReadResponse createCommentOnSchedule(Long scheduleSeq,
        Long userSeq, CommentCreateRequest request) {

        Schedule schedule = findScheduleInfo(scheduleSeq);

        User user = findUserInfo(userSeq);

        Comment comment = commentRepository.save(request.toEntity(user, schedule));

        return CommentReadResponse.of(comment, UserReadResponse.of(user));
    }

    public CategoryReadResponse createCategory(Long userSeq, CategoryCreateRequest request) {

        // 사용자 체크
        User user = findUserInfo(userSeq);

        // 중복 체크
        checkCategoryNameAlreadyExists(userSeq, request.categoryName());

        Category category = categoryRepository.save(request.toEntity(user));

        return CategoryReadResponse.of(category);
    }

    public List<CategoryReadResponse> getCategoryList(Long userSeq) {

        return findCategoryListByUser(userSeq);
    }

    @Transactional
    public void setMentionAlarm(Long userSeq, Long scheduleSeq, boolean isEnabled) {
        User user = findUserInfo(userSeq);
        Schedule schedule = findScheduleInfo(scheduleSeq);
        Participation participation = findParticipationInfo(scheduleSeq, userSeq);
        participation.updateIsMentionAlarmOn(isEnabled);
    }

    @Transactional
    public void setUpdateAlarm(Long userSeq, Long scheduleSeq, boolean isEnabled) {
        User user = findUserInfo(userSeq);
        Schedule schedule = findScheduleInfo(scheduleSeq);
        Participation participation = findParticipationInfo(scheduleSeq, userSeq);
        participation.updateIsUpdateAlarmOn(isEnabled);
    }

    // ================== private methods ==================

    private void createParticipation(Long userSeq, ScheduleCreateRequest request,
        Schedule newSchedule) {
        request.participants().forEach(response -> {
            // seq로 user 찾기
            User participant = findUserInfo(response.userSeq());

            // 카테고리 정보가 있는 경우 참석자가 생성자라면 카테고리 정보 입력
            Category category = null;
            if ((request.categorySeq() != null) && Objects.equals(participant.getUserSeq(),
                userSeq)) {
                category = findCategoryInfo(request.categorySeq());
            }

            Participation participation = Participation.create(request, participant, newSchedule,
                category, true, true);

            participationRepository.save(participation);
        });
    }

    private User findUserInfo(Long userSeq) {
        return userRepository.findByUserSeq(userSeq).orElseThrow(
            () -> new RestApiException(UserErrorCode.USER_NOT_FOUND));
    }

    private Schedule findScheduleInfo(Long scheduleSeq) {
        return scheduleRepository.findByScheduleSeqAndIsDeletedFalse(scheduleSeq)
            .orElseThrow(() -> new RestApiException(ScheduleErrorCode.SCHEDULE_NOT_FOUND));
    }

    private Participation findParticipationInfo(Long scheduleSeq, Long userSeq) {
        return participationRepository.findBySchedule_ScheduleSeqAndUser_UserSeqAndIsDeletedFalse(
                scheduleSeq, userSeq)
            .orElseThrow(() -> new RestApiException(ScheduleErrorCode.SCHEDULE_NOT_FOUND));
    }

    private Category findCategoryInfo(Long categorySeq) {
        return categoryRepository.findByCategorySeqAndIsDeletedFalse(categorySeq)
            .orElseThrow(() -> new RestApiException(CategoryErrorCode.CATEGORY_NOT_FOUND));
    }

    private ScheduleListReadResponse findScheduleByParticipation(
        Participation participation) {

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
            .toList();

        // 3. 전체에게 권한이 있는 경우 authorizedUser 필드는 null로 만들어 response 반환
        if (schedule.isAuthorizedAll()) {
            return ScheduleListReadResponse.of(schedule, null, participants,
                participation);
        }
        return ScheduleListReadResponse.of(schedule,
            UserReadResponse.of(schedule.getAuthorizedUser()), participants, participation);
    }

    private List<UserReadResponse> findParticipatorListByUserName(Long scheduleSeq,
        String searchKey) {
        // 1. 일정 유효 체크
        findScheduleInfo(scheduleSeq);

        // 2. 참가자 명단 가져오기
        return participationRepository.findBySchedule_ScheduleSeqAndIsDeletedFalse(scheduleSeq)
            .stream()
            .map(participation -> UserReadResponse.of(participation.getUser()))
            .filter(user -> user.userName().contains(searchKey))
            .toList();
    }

    private List<CommentReadResponse> findCommentListBySchedule(Long scheduleSeq) {
        return commentRepository.findBySchedule_ScheduleSeqAndIsDeletedFalse(scheduleSeq)
            .stream()
            .map(comment -> CommentReadResponse.of(comment, UserReadResponse.of(comment.getUser())))
            .toList();
    }

    private List<CategoryReadResponse> findCategoryListByUser(Long userSeq) {
        return categoryRepository.findByUser_UserSeqAndIsDeletedFalse(userSeq)
            .stream()
            .map(CategoryReadResponse::of)
            .toList();
    }

    private void checkCategoryNameAlreadyExists(Long userSeq, String categoryName) {
        if (categoryRepository.existsByUser_UserSeqAndCategoryNameAndIsDeletedFalse(userSeq,
            categoryName)) {
            throw new RestApiException(CategoryErrorCode.CATEGORY_ALREADY_EXISTS);
        }
    }

    private List<ScheduleListReadResponse> findScheduleListBySearchCondition(Long userSeq,
        Map<String, String> searchCondition) {
        // 사용자 체크
        findUserInfo(userSeq);

        // 검색조건 확인
        String categorySeqList = searchCondition.get("categories");
        String searchKey = searchCondition.get("searchKey");
        String unscheduled = searchCondition.get("unscheduled");

        return participationRepository.findByUser_UserSeqAndIsDeletedFalse(userSeq)
            .stream()
            // filter 1. 카테고리
            .filter(participation -> {
                if (!categorySeqList.isEmpty()) {

                    // categorySeq 목록 가져오기
                    List<Long> categorySeqs = Arrays.stream(
                            categorySeqList.split(","))
                        .map(Long::parseLong)
                        .toList();

                    // 해당 카테고리에 포함되는 경우만 반환
                    return participation.getCategory() != null && categorySeqs.contains(
                        participation.getCategory().getCategorySeq());
                }
                // false인 경우 모든 경우 반환
                return true;
            })
            // filter 2. 일정명
            .filter(participation -> {
                if (!searchKey.isEmpty()) {
                    return participation.getScheduleName()
                        .contains(searchKey);
                }
                return true;
            })
            // filter 3. 미정 일정
            .filter(participation -> {
                if (!unscheduled.isEmpty()) {
                    return !participation.getSchedule().isDateSelected();
                }
                return true;
            })
            // TODO: 월별, 주별 일정 반환할 시 필터 추가 필요
            .map(this::findScheduleByParticipation)
            .toList();
    }
}
