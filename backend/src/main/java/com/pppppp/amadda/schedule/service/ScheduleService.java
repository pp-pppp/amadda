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
import com.pppppp.amadda.schedule.entity.CategoryColor;
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

    @Transactional
    public ScheduleCreateResponse createSchedule(User user, ScheduleCreateRequest request) {

        Schedule newSchedule = request.toEntity(user);

        scheduleRepository.save(newSchedule);

        List<Participation> newParticipations = new LinkedList<>();

        request.participants().forEach(response -> {
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

        // 댓글 가져오기
        List<CommentReadResponse> comments = commentRepository.findBySchedule_ScheduleSeqAndIsDeletedFalse(
                scheduleSeq)
            .stream()
            .map(comment -> CommentReadResponse.of(comment, UserReadResponse.of(comment.getUser())))
            .collect(Collectors.toList());

        return ScheduleDetailReadResponse.of(schedule, participants, participation, comments);
    }

    public List<ScheduleListReadResponse> getScheduleList(User user) {
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

    // TODO: 동적 쿼리를 통한 일정 검색 메소드 테스트 구현

    @Transactional
    public CommentReadResponse createCommentsOnSchedule(Long scheduleSeq,
        User user, CommentCreateRequest request) {
        Schedule schedule = scheduleRepository.findByScheduleSeqAndIsDeletedFalse(scheduleSeq)
            .orElseThrow(() -> new RestApiException(ScheduleErrorCode.SCHEDULE_NOT_FOUND));

        Comment comment = Comment.builder()
            .user(user)
            .schedule(schedule)
            .commentContent(request.commentContent())
            .build();

        commentRepository.save(comment);

        return CommentReadResponse.of(comment, UserReadResponse.of(user));
    }

    public CategoryReadResponse createCategory(User user, CategoryCreateRequest request) {

        Category category = Category.builder()
            .user(user)
            .categoryName(request.categoryName())
            .categoryColor(CategoryColor.valueOf(request.categoryColor()))
            .build();

        // 중복체크
        if (categoryRepository.existsByUser_UserSeqAndCategoryNameAndIsDeletedFalse(
            user.getUserSeq(), category.getCategoryName())) {
            throw new RestApiException(CategoryErrorCode.CATEGORY_ALREADY_EXISTS);
        }

        categoryRepository.save(category);

        return CategoryReadResponse.of(category);
    }

    public List<CategoryReadResponse> getCategoryList(User user) {
        List<Category> categories = categoryRepository.findByUser_UserSeqAndIsDeletedFalse(
            user.getUserSeq());

        return categories.stream()
            .map(CategoryReadResponse::of)
            .collect(Collectors.toList());
    }
}
