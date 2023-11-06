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
import java.util.Objects;
import java.util.stream.Stream;
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

    public List<ScheduleListReadResponse> getScheduleList(Long userSeq) {

        // 사용자 체크
        findUserInfo(userSeq);

        // 요청한 사용자의 참가 정보 모두 가져오기
        return findScheduleListByUser(userSeq);
    }

    public List<ScheduleListReadResponse> getSearchResultByScheduleName(Long userSeq,
        String searchKey) {

        // 사용자 체크
        findUserInfo(userSeq);

        // 요청한 사용자의 참가 정보 중에 searchKey를 포함하는 일정들 가져오고, 참가 정보를 바탕으로 일정 리스트 만들어서 반환
        return findScheduleListByScheduleName(userSeq, searchKey);
    }

    public List<ScheduleListReadResponse> getScheduleByCategoryList(Long userSeq,
        String categories) {
        // 카테고리 seq 목록
        List<Long> categorySeqs = Stream.of(categories.split(","))
            .map(Long::parseLong)
            .toList();

        List<ScheduleListReadResponse> schedules = new LinkedList<>();

        categorySeqs.forEach(categorySeq -> {
            // 1. 해당 카테고리 seq가 유효한지 확인
            checkCategoryExistsByUser(userSeq, categorySeq);

            // 2. 카테고리에 포함된 일정 가져오기
            List<ScheduleListReadResponse> categorySchedules = findScheduleListByCategory(userSeq,
                categorySeq);

            // 3. 목록에 추가
            schedules.addAll(categorySchedules);
        });

        // 전체 일정 목록 반환
        return schedules;
    }

    public List<UserReadResponse> getParticipatingUserListBySearchKey(Long scheduleSeq,
        String searchKey) {

        return findParticipatorListByUserName(scheduleSeq, searchKey);
    }

    public List<ScheduleListReadResponse> getUnscheduledScheduleList(Long userSeq) {

        return findUnscheduledScheduleList(userSeq);
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
                category);

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

    private List<ScheduleListReadResponse> findScheduleListByUser(Long userSeq) {
        return participationRepository.findByUser_UserSeqAndIsDeletedFalse(userSeq)
            .stream()
            .map(this::findScheduleByParticipation)
            .toList();
    }

    private List<ScheduleListReadResponse> findScheduleListByScheduleName(Long userSeq,
        String searchKey) {
        return participationRepository.findByUser_UserSeqAndScheduleNameContainingAndIsDeletedFalse(
                userSeq, searchKey)
            .stream()
            .map(this::findScheduleByParticipation)
            .toList();
    }

    private List<ScheduleListReadResponse> findScheduleListByCategory(Long userSeq,
        Long categorySeq) {
        return participationRepository.findByUser_UserSeqAndCategory_CategorySeqAndIsDeletedFalse(
                userSeq, categorySeq)
            .stream()
            .map(this::findScheduleByParticipation)
            .toList();
    }

    private List<ScheduleListReadResponse> findUnscheduledScheduleList(Long userSeq) {

        return participationRepository.findByUser_UserSeqAndIsDeletedFalse(userSeq)
            .stream()
            .filter(participation -> !participation.getSchedule().isDateSelected())
            .map(this::findScheduleByParticipation)
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

    private void checkCategoryExistsByUser(Long userSeq, Long categorySeq) {
        if (!categoryRepository.existsByUser_UserSeqAndCategorySeqAndIsDeletedFalse(userSeq,
            categorySeq)) {
            throw new RestApiException(CategoryErrorCode.CATEGORY_NOT_FOUND);
        }
    }

    private void checkCategoryNameAlreadyExists(Long userSeq, String categoryName) {
        if (categoryRepository.existsByUser_UserSeqAndCategoryNameAndIsDeletedFalse(userSeq,
            categoryName)) {
            throw new RestApiException(CategoryErrorCode.CATEGORY_ALREADY_EXISTS);
        }
    }
}
