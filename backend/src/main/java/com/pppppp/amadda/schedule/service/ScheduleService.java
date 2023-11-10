package com.pppppp.amadda.schedule.service;

import com.pppppp.amadda.alarm.service.AlarmService;
import com.pppppp.amadda.global.entity.exception.RestApiException;
import com.pppppp.amadda.global.entity.exception.errorcode.CategoryErrorCode;
import com.pppppp.amadda.global.entity.exception.errorcode.CommentErrorCode;
import com.pppppp.amadda.global.entity.exception.errorcode.ScheduleErrorCode;
import com.pppppp.amadda.global.entity.exception.errorcode.UserErrorCode;
import com.pppppp.amadda.schedule.dto.request.CategoryCreateRequest;
import com.pppppp.amadda.schedule.dto.request.CategoryPatchRequest;
import com.pppppp.amadda.schedule.dto.request.CommentCreateRequest;
import com.pppppp.amadda.schedule.dto.request.ScheduleCreateRequest;
import com.pppppp.amadda.schedule.dto.request.SchedulePatchRequest;
import com.pppppp.amadda.schedule.dto.response.CategoryReadResponse;
import com.pppppp.amadda.schedule.dto.response.CommentReadResponse;
import com.pppppp.amadda.schedule.dto.response.ScheduleCreateResponse;
import com.pppppp.amadda.schedule.dto.response.ScheduleDetailReadResponse;
import com.pppppp.amadda.schedule.dto.response.ScheduleListReadResponse;
import com.pppppp.amadda.schedule.entity.AlarmTime;
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
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
@Transactional(readOnly = true)
public class ScheduleService {

    private final AlarmService alarmService;

    private final ScheduleRepository scheduleRepository;

    private final ParticipationRepository participationRepository;

    private final UserRepository userRepository;

    private final CommentRepository commentRepository;

    private final CategoryRepository categoryRepository;


    // ================== schedule & participation ==================
    @Transactional
    public ScheduleCreateResponse createSchedule(Long userSeq, ScheduleCreateRequest request) {
        User user = findUserInfo(userSeq);

        // 시간 입력이 잘 됐는지 확인
        checkTimeInput(request);

        // request to entity
        Schedule newSchedule = scheduleRepository.save(request.toEntity(user));

        // 참가정보 바탕으로 참가자별 참석 정보 생성
        createParticipation(userSeq, request, newSchedule);

        // 생성한 사람의 일정의 개인 기록 가져오기
        Participation creatorParticipation = findParticipationInfoBySchedule(
            newSchedule.getScheduleSeq(),
            userSeq);

        return ScheduleCreateResponse.of(newSchedule, request.participants(),
            creatorParticipation);
    }

    public String getServerTime() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

        return LocalDateTime.now().format(formatter);
    }

    public ScheduleDetailReadResponse getScheduleDetail(Long scheduleSeq, Long userSeq) {
        findUserInfo(userSeq);
        Schedule schedule = findScheduleInfo(scheduleSeq);

        // 명단 가져오기
        List<UserReadResponse> participants = getParticipatingUserList(scheduleSeq);

        // 일정 생성
        Participation participation = findParticipationInfoBySchedule(scheduleSeq, userSeq);

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

    @Transactional
    public ScheduleDetailReadResponse updateSchedule(Long userSeq, Long scheduleSeq,
        SchedulePatchRequest request) {
        findUserInfo(userSeq);
        Schedule schedule = findScheduleInfo(scheduleSeq);

        // 사용자가 권한이 없으면 안됨
        checkUserAuthorizedToSchedule(userSeq, schedule);

        // 1. 전체 참가자에게 동기화되는 일정 정보 수정
        schedule.updateScheduleInfo(request);

        // 2. 참가자 참석 정보 가져오기
        Participation participation = findParticipationInfoBySchedule(scheduleSeq,
            userSeq);

        // 3. 개인 참석 정보 수정
        participation.updateParticipationInfo(request);

        // 4. 추가되는 사용자가 있으면 새롭게 참가자 추가, 없으면 참가자 목록 수정
        updateParticipantList(userSeq, request, schedule);

        return getScheduleDetail(scheduleSeq, userSeq);
    }

    @Transactional
    public void addScheduleToCategory(Long userSeq, Long scheduleSeq,
        Long categorySeq) {
        findUserInfo(userSeq);
        Participation participation = findParticipationInfoBySchedule(scheduleSeq, userSeq);
        Category category = findCategoryInfo(categorySeq);

        // 요청을 보낸 사람이 참가자가 아니면 안됨
        checkUserParticipation(userSeq, participation);

        // 요청을 보낸 사람이 카테고리 주인이 아니면 안됨
        checkUserAuthorizedToCategory(userSeq, category);

        // 참가정보 업데이트
        participation.updateCategory(category);
    }

    @Transactional
    public void deleteScheduleFromCategory(Long userSeq,
        Long scheduleSeq, Long categorySeq) {
        findUserInfo(userSeq);
        Participation participation = findParticipationInfoBySchedule(scheduleSeq, userSeq);

        Category category = findCategoryInfo(categorySeq);

        // 요청을 보낸 사람이 참가자가 아니면 안됨
        checkUserParticipation(userSeq, participation);

        // 요청을 보낸 사람이 카테고리 주인이 아니면 안됨
        checkUserAuthorizedToCategory(userSeq, category);

        // 참가정보 업데이트
        participation.updateCategory(null);
    }

    @Transactional
    public void deleteParticipation(Long requestUserSeq, Schedule schedule) {
        // 유효 체크
        Participation participation = findParticipationInfoBySchedule(
            schedule.getScheduleSeq(), requestUserSeq);

        // 권한 체크, 본인이거나 일정 생성자면 삭제 가능
        checkUserAuthorizedToParticipation(requestUserSeq, participation);

        participationRepository.delete(participation);
    }


    @Transactional
    public void deleteSchedule(Long userSeq, Long scheduleSeq) {
        findUserInfo(userSeq);
        Schedule schedule = findScheduleInfo(scheduleSeq);

        // 사용자가 권한이 없으면 안됨
        checkUserAuthorizedToSchedule(userSeq, schedule);

        // 연결되어 있는 참가정보 삭제
        List<Participation> participations = findParticipationListBySchedule(scheduleSeq);
        participationRepository.deleteAllInBatch(participations);

        scheduleRepository.delete(schedule);
    }

    // ================== comment ==================

    @Transactional
    public CommentReadResponse createCommentOnSchedule(Long scheduleSeq,
        Long userSeq, CommentCreateRequest request) {

        Schedule schedule = findScheduleInfo(scheduleSeq);
        User user = findUserInfo(userSeq);

        Comment comment = commentRepository.save(request.toEntity(user, schedule));

        return CommentReadResponse.of(comment, UserReadResponse.of(user));
    }

    @Transactional
    public void deleteComment(Long commentSeq, Long userSeq) {
        Comment comment = findCommentInfo(commentSeq);

        // 요청을 보낸 사람이 댓글 작성자가 아니면 안됨
        checkUserAuthorizedToComment(userSeq, comment);

        commentRepository.delete(comment);
    }

    // ================== category ==================

    @Transactional
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
    public CategoryReadResponse updateCategory(Long userSeq, Long categorySeq,
        CategoryPatchRequest request) {

        // 카테고리 유효 체크
        Category category = findCategoryInfo(categorySeq);

        // 요청한 사용자와 카테고리 주인이 다르면 안됨
        checkUserAuthorizedToCategory(userSeq, category);

        category.updateCategoryInfo(request);

        return CategoryReadResponse.of(category);
    }

    @Transactional
    public void deleteCategory(Long userSeq, Long categorySeq) {

        // 카테고리 유효 체크
        Category category = findCategoryInfo(categorySeq);

        // 요청한 사용자와 카테고리 주인이 다르면 안됨
        checkUserAuthorizedToCategory(userSeq, category);

        // 카테고리에 포함되어 있는 일정 카테고리 설정 해제
        deleteCategoryInfoInParticipation(categorySeq);

        categoryRepository.delete(category);
    }

    @Transactional
    public void setMentionAlarm(Long userSeq, Long scheduleSeq, boolean isEnabled) {
        findUserInfo(userSeq);
        findScheduleInfo(scheduleSeq);
        Participation participation = findParticipationInfoBySchedule(scheduleSeq, userSeq);
        participation.updateIsMentionAlarmOn(isEnabled);
    }

    @Transactional
    public void setUpdateAlarm(Long userSeq, Long scheduleSeq, boolean isEnabled) {
        findUserInfo(userSeq);
        findScheduleInfo(scheduleSeq);
        Participation participation = findParticipationInfoBySchedule(scheduleSeq, userSeq);
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

            if (!Objects.equals(participant.getUserSeq(), userSeq)) {
                alarmService.sendScheduleAssigned(
                    participation.getSchedule().getScheduleSeq(),
                    userSeq, participation.getUser().getUserSeq());
            }
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

    private Participation findParticipationInfoBySchedule(Long scheduleSeq, Long userSeq) {
        return participationRepository.findBySchedule_ScheduleSeqAndUser_UserSeqAndIsDeletedFalse(
                scheduleSeq, userSeq)
            .orElseThrow(() -> new RestApiException(ScheduleErrorCode.SCHEDULE_NOT_FOUND));
    }

    private Comment findCommentInfo(Long commentSeq) {
        return commentRepository.findByCommentSeq(commentSeq)
            .orElseThrow(() -> new RestApiException(CommentErrorCode.COMMENT_NOT_FOUND));
    }

    private Category findCategoryInfo(Long categorySeq) {
        return categoryRepository.findByCategorySeqAndIsDeletedFalse(categorySeq)
            .orElseThrow(() -> new RestApiException(CategoryErrorCode.CATEGORY_NOT_FOUND));
    }

    private void checkTimeInput(ScheduleCreateRequest request) {
        // 1. isDateSelected = true인데 시작 시간 입력이 안 됐으면 예외 처리
        if (request.isDateSelected() && request.scheduleStartAt() == null) {
            throw new RestApiException(ScheduleErrorCode.SCHEDULE_DATE_NOT_SELECTED);
        }

        // 2. isTimeSelected == true인데 시작 시간 또는 종료 시간 입력이 안 됐으면 예외 처리
        if (request.isTimeSelected() && ((request.scheduleStartAt() == null) || (
            request.scheduleEndAt() == null))) {
            throw new RestApiException(ScheduleErrorCode.SCHEDULE_TIME_NOT_SELECTED);
        }
    }

    private void checkUserAuthorizedToSchedule(Long userSeq, Schedule schedule) {
        if (!(schedule.isAuthorizedAll() || schedule.getAuthorizedUser().getUserSeq()
            .equals(userSeq))) {
            throw new RestApiException(ScheduleErrorCode.SCHEDULE_FORBIDDEN);
        }
    }

    private void checkUserAuthorizedToParticipation(Long userSeq, Participation particiption) {
        if (!(particiption.getUser().getUserSeq().equals(userSeq) || particiption.getSchedule()
            .getAuthorizedUser().getUserSeq().equals(userSeq))) {
            throw new RestApiException(ScheduleErrorCode.SCHEDULE_FORBIDDEN);
        }
    }

    private void checkUserParticipation(Long userSeq, Participation participation) {
        if (!participation.getUser().getUserSeq().equals(userSeq)) {
            throw new RestApiException(ScheduleErrorCode.SCHEDULE_FORBIDDEN);
        }
    }

    private void checkUserAuthorizedToComment(Long userSeq, Comment comment) {
        if (!comment.getUser().getUserSeq().equals(userSeq)) {
            throw new RestApiException(CommentErrorCode.COMMENT_FORBIDDEN);
        }
    }

    private void checkUserAuthorizedToCategory(Long userSeq, Category category) {

        if (!category.getUser().getUserSeq().equals(userSeq)) {
            throw new RestApiException(CategoryErrorCode.CATEGORY_FORBIDDEN);
        }
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

    private List<Participation> findParticipationListBySchedule(Long scheduleSeq) {

        return participationRepository.findBySchedule_ScheduleSeqAndIsDeletedFalse(scheduleSeq);
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

    private void updateParticipantList(Long requestUserSeq, SchedulePatchRequest request,
        Schedule schedule) {

        // 1. 이전 사용자 목록
        List<User> previousParticipationList = participationRepository.findBySchedule_ScheduleSeqAndIsDeletedFalse(
                schedule.getScheduleSeq())
            .stream()
            .map(Participation::getUser)
            .toList();

        // 2. 수정할 사용자 목록
        List<User> updateParticipationList = request.participants()
            .stream()
            .map(response -> findUserInfo(response.userSeq()))
            .toList();

        // 3. 수정할 사용자 목록과 비교해서 현재 사용자 중 삭제된 사용자가 있는지 확인, 있으면 참석정보 삭제
        previousParticipationList.stream()
            .filter(user -> !updateParticipationList.contains(user))
            .forEach(user -> deleteParticipation(user.getUserSeq(), schedule));

        // 4. 이전 사용자 목록과 비교해서 현재 사용자 중 추가된 사용자가 있는지 확인, 있으면 참석정보 생성.
        Participation requestUserParticipation = findParticipationInfoBySchedule(
            schedule.getScheduleSeq(), requestUserSeq);

        // 생성할 참석 정보는 이번 요청으로 수정되는 필드가 아니면 요청자의 참석정보를 기본값으로 사용
        String scheduleName = (request.scheduleName() != null) ? request.scheduleName()
            : requestUserParticipation.getScheduleName();
        AlarmTime alarmTime = (request.alarmTime() != null) ? request.alarmTime()
            : requestUserParticipation.getAlarmTime();
        ScheduleCreateRequest createRequest = ScheduleCreateRequest.builder()
            .scheduleName(scheduleName)
            .alarmTime(alarmTime)
            .build();

        updateParticipationList.stream()
            .filter(user -> !previousParticipationList.contains(user))
            .forEach(user -> {
                Participation participation = Participation.create(createRequest, user, schedule,
                    null, true, true);
                participationRepository.save(participation);
                alarmService.sendScheduleAssigned(schedule.getScheduleSeq(), requestUserSeq,
                    user.getUserSeq());
            });
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
        String year = searchCondition.get("year");
        String month = searchCondition.get("month");
        String day = searchCondition.get("day");

        // 날짜 조건 입력이 잘 됐는지 확인
        checkDateConditionValid(year, month, day);

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
            // filter 4. 연도별 일정
            .filter(participation -> {
                if (!year.isEmpty() && month.isEmpty() && day.isEmpty()) {
                    return checkScheduleInYearCondition(participation, year);
                }
                return true;
            })
            // filter 5. 월별 일정
            .filter(participation -> {
                if (!month.isEmpty() && day.isEmpty()) {
                    return checkScheduleInMonthCondition(participation, year, month);
                }
                return true;
            })
            // filter 6. 일별 일정
            .filter(participation -> {
                if (!day.isEmpty()) {
                    return checkScheduleInDayCondition(participation, year, month, day);
                }
                return true;
            })
            .map(this::findScheduleByParticipation)
            .toList();
    }

    private void checkDateConditionValid(String year, String month, String day) {
        // 만약 날짜 조건으로 검색하는데 연도가 없으면 예외 처리
        if (year.isEmpty() && !(month.isEmpty() && day.isEmpty())) {
            throw new RestApiException(ScheduleErrorCode.SCHEDULE_INVALID_REQUEST);
        }

        // 만약 일별 조회인데 월 단위 입력이 없으면 예외 처리
        if (month.isEmpty() && !day.isEmpty()) {
            throw new RestApiException(ScheduleErrorCode.SCHEDULE_INVALID_REQUEST);
        }
    }

    private boolean checkScheduleInYearCondition(Participation participation, String year) {

        // 확인하려는 일정의 시작시점, 종료시점
        LocalDateTime scheduleStartAt = participation.getSchedule().getScheduleStartAt();
        LocalDateTime scheduleEndAt = participation.getSchedule().getScheduleEndAt();

        LocalTime startOfTheDay = LocalTime.of(0, 0, 0);
        LocalTime endOfTheDay = LocalTime.of(23, 59, 59);

        // 일정 시작 연도, 종료 연도가 해당 연도 이거나 해당 연도가 일정 중에 포함 되면 true
        return
            (scheduleStartAt.isBefore(
                LocalDateTime.of(LocalDate.of(Integer.parseInt(year), 1, 1), startOfTheDay))
                && scheduleEndAt.isAfter(
                LocalDateTime.of(LocalDate.of(Integer.parseInt(year), 12, 31), endOfTheDay)))
                || scheduleStartAt.getYear() == Integer.parseInt(year)
                || scheduleEndAt.getYear() == Integer.parseInt(year);
    }

    private boolean checkScheduleInMonthCondition(Participation participation, String year,
        String month) {
        // 확인하려는 일정의 시작시점, 종료시점
        LocalDateTime scheduleStartAt = participation.getSchedule().getScheduleStartAt();
        LocalDateTime scheduleEndAt = participation.getSchedule().getScheduleEndAt();

        LocalTime startOfTheDay = LocalTime.of(0, 0, 0);
        LocalTime endOfTheDay = LocalTime.of(23, 59, 59);

        // 해당 년도의 해당 월의 마지막 날짜를 알기 위해 Calender 객체 생성
        Calendar calendar = Calendar.getInstance();
        calendar.set(Integer.parseInt(year), Integer.parseInt(month) - 1, 1);

        LocalDateTime startTime = LocalDate.of(Integer.parseInt(year),
            Integer.parseInt(month),
            1).atTime(startOfTheDay);
        LocalDateTime endTime = LocalDate.of(calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.getActualMaximum(Calendar.DAY_OF_MONTH)).atTime(endOfTheDay);

        // 일정 시작 시점이나 종료 시점이 해당 달이거나 해당 달이 일정 중에 포함되면 true
        return (scheduleStartAt.getYear() == Integer.parseInt(year)
            && scheduleStartAt.getMonthValue() == Integer.parseInt(month))
            || (scheduleEndAt.getYear() == Integer.parseInt(year)
            && scheduleEndAt.getMonthValue() == Integer.parseInt(month))
            || (scheduleStartAt.isBefore(startTime) && scheduleEndAt.isAfter(endTime));
    }

    private boolean checkScheduleInDayCondition(Participation participation, String year,
        String month, String day) {

        // 확인하려는 일정의 시작시점, 종료시점
        LocalDateTime scheduleStartAt = participation.getSchedule().getScheduleStartAt();
        LocalDateTime scheduleEndAt = participation.getSchedule().getScheduleEndAt();

        LocalTime startOfTheDay = LocalTime.of(0, 0, 0);
        LocalTime endOfTheDay = LocalTime.of(23, 59, 59);

        LocalDateTime startTime = LocalDate.of(Integer.parseInt(year),
            Integer.parseInt(month),
            Integer.parseInt(day)).atTime(startOfTheDay);
        LocalDateTime endTime = LocalDate.of(Integer.parseInt(year),
            Integer.parseInt(month),
            Integer.parseInt(day)).atTime(endOfTheDay);

        // 일정 시작 시점이나 종료 시점이 해당 날짜이거나 해당 날짜가 일정 중에 포함되면 true
        return (scheduleStartAt.getYear() == Integer.parseInt(year)
            && scheduleStartAt.getMonthValue() == Integer.parseInt(month)
            && scheduleStartAt.getDayOfMonth() == Integer.parseInt(day))
            || (scheduleEndAt.getYear() == Integer.parseInt(year)
            && scheduleEndAt.getMonthValue() == Integer.parseInt(month)
            && scheduleEndAt.getDayOfMonth() == Integer.parseInt(day)) || (
            scheduleStartAt.isBefore(startTime) && scheduleEndAt.isAfter(endTime));
    }

    private void deleteCategoryInfoInParticipation(Long categorySeq) {
        participationRepository.findByCategory_CategorySeqAndIsDeletedFalse(categorySeq)
            .forEach(participation -> participation.updateCategory(null));
    }
}
