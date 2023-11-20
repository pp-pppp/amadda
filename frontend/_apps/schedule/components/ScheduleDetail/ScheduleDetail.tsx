import React, { useState } from 'react';
import {
  CommentCreateRequest,
  ScheduleDetailReadResponse,
} from 'amadda-global-types';
import {
  Chip,
  Flex,
  H2,
  P,
  Icon,
  Spacing,
  Span,
  Profile,
  Input,
  Btn,
  Textarea,
} from 'external-temporal';
import { http } from '@SCH/utils/http';
import { InferGetServerSidePropsType } from 'next';
import { getServerSideProps } from '@SCH/pages/schedule/[id]';
import CALENDAR from '@SCH/constants/CALENDAR';
import { BASE, BUTTON, GRID } from './ScheduleDetail.css';
import { Category } from '../ScheduleEdit/Category/Category';

export function ScheduleDetail({
  detail,
}: InferGetServerSidePropsType<typeof getServerSideProps>) {
  const [comment, setComment] = useState('');

  //dummy
  detail = {
    scheduleSeq: 3,
    scheduleContent: '얼굴 본지 몇달째냐',
    isTimeSelected: true,
    isDateSelected: true,
    isAllday: false,
    isAuthorizedAll: false,
    scheduleStartAt: '2023-11-25 19-00-00',
    scheduleEndAt: '2023-11-25 21-00-00',
    authorizedUser: {
      userSeq: 1,
      userName: '유승윤',
      userId: 'brewcoldblue',
      imageUrl:
        'https://amadda-bucket.s3.ap-northeast-2.amazonaws.com/1111_2023-11-10-15-04-51.jpg',
    },
    participants: [
      {
        userSeq: 2,
        userName: '권소희',
        userId: 'hohee-hee',
        imageUrl:
          'https://amadda-bucket.s3.ap-northeast-2.amazonaws.com/1111_2023-11-10-15-04-51.jpg',
        isFriend: true,
      },
      {
        userSeq: 3,
        userName: '정민영',
        userId: 'darkard',
        imageUrl:
          'https://amadda-bucket.s3.ap-northeast-2.amazonaws.com/1111_2023-11-10-15-04-51.jpg',
        isFriend: true,
      },
    ],
    comments: [
      // {
      //   commentSeq: 1,
      //   user: {
      //     userSeq: 3,
      //     userName: '정민영',
      //     userId: 'darkard',
      //     imageUrl:
      //       'https://amadda-bucket.s3.ap-northeast-2.amazonaws.com/1111_2023-11-10-15-04-51.jpg',
      //   },
      //   commentContent: '야호!',
      //   createdAt: '2023-11-10 10:12:23',
      // },
      // {
      //   commentSeq: 2,
      //   user: {
      //     userSeq: 1,
      //     userName: '유승윤',
      //     userId: 'brewcoldblue',
      //     imageUrl:
      //       'https://amadda-bucket.s3.ap-northeast-2.amazonaws.com/1111_2023-11-10-15-04-51.jpg',
      //   },
      //   commentContent: '뭐 먹어',
      //   createdAt: '2023-11-10 10:15:24',
      // },
    ],

    alarmTime: '30분 전',
    scheduleMemo: '선릉 병기오빠 회사 앞에서 보기로함',
    scheduleName: '최고의 쿠팡맨 민 병 기',
    category: {
      categorySeq: 1,
      categoryName: 'ssafy',
      categoryColor: 'SALMON',
    },
  };

  const createComment = e => {
    const CommentCreateRequestBody: CommentCreateRequest = {
      commentContent: comment,
    };

    http
      .post(
        `${process.env.PUBLIC_NEXT_SCHEDULE}/api/schedule/${detail.scheduleSeq}/comment`,
        CommentCreateRequestBody
      )
      .then(res => setComment(''));
  };

  return (
    <>
      <H2>{detail.scheduleName}</H2>
      <Spacing dir="v" size="2" />
      <Span>참가자</Span>
      <Spacing dir="v" size="0.5" />
      {detail.participants.map((participant, idx) => (
        <Chip
          key={idx}
          type="suggestion"
          label={participant.userName}
          shape="square"
        />
      ))}
      <Spacing dir="v" size="2" />
      <Span>날짜</Span>
      <Spacing dir="v" size="0.5" />
      {!detail.isDateSelected ? (
        <Chip type="suggestion" label={CALENDAR.NO_DATE} shape="square" />
      ) : (
        <div className={BASE}>
          <Span>
            {detail.scheduleStartAt.split(' ')[0].split('-')[0]}년{' '}
            {detail.scheduleStartAt.split(' ')[0].split('-')[1]}월{' '}
            {detail.scheduleStartAt.split(' ')[0].split('-')[2]}일
          </Span>
        </div>
      )}

      <Spacing dir="v" size="2" />
      <Span>시간</Span>
      <Spacing dir="v" size="0.5" />
      {!detail.isTimeSelected ? (
        <Chip type="suggestion" label={CALENDAR.NO_TIME} shape="square" />
      ) : detail.isAllday ? (
        <Chip type="suggestion" label={CALENDAR.ALLDAY} shape="square" />
      ) : (
        <Flex justifyContents="start">
          <div className={BASE}>
            <Span>
              {detail.scheduleStartAt.split(' ')[1].split('-')[0]}시{' '}
              {detail.scheduleStartAt.split(' ')[1].split('-')[1]}분{' '}
            </Span>
          </div>
          <Spacing dir="h" size="0.5" />
          <Span>~</Span>
          <Spacing dir="h" size="0.5" />
          <div className={BASE}>
            <Span>
              {detail.scheduleEndAt.split(' ')[1].split('-')[0]}시{' '}
              {detail.scheduleEndAt.split(' ')[1].split('-')[1]}분{' '}
            </Span>
          </div>
        </Flex>
      )}
      <Spacing dir="v" size="2" />
      <Span>상세 내용</Span>
      <Spacing dir="v" size="0.5" />
      <div className={BASE}>
        <P>{detail.scheduleContent}</P>
      </div>
      <Spacing dir="v" size="2" />
      <Span>알림 설정</Span>
      <Spacing dir="v" size="0.5" />
      <Chip type="suggestion" label={detail.alarmTime} shape="square" />
      <Spacing dir="v" size="2" />
      <Span>카테고리</Span>
      <Spacing dir="v" size="0.5" />
      <Category
        color={detail.category.categoryColor}
        categoryName={detail.category.categoryName}
      />
      <Spacing dir="v" size="2" />
      <Span>메모</Span>
      <Spacing dir="v" size="0.5" />
      <div className={BASE}>
        <P>{detail.scheduleMemo}</P>
      </div>
      <Spacing dir="v" size="2" />
      <Span>댓글</Span>
      <Spacing dir="v" size="0.5" />
      {detail.comments.length === 0 ? (
        <Span color="lightgrey">아직 댓글이 없어요!</Span>
      ) : (
        detail.comments.map((comment, idx) => {
          return (
            <>
              <div className={GRID}>
                <Profile
                  src={comment.user.imageUrl}
                  alt={comment.user.userSeq.toString()}
                />
                <Flex
                  justifyContents="start"
                  alignItems="start"
                  flexDirection="column"
                >
                  <Span>{comment.user.userName}</Span>
                </Flex>
                <Span>{comment.commentContent}</Span>
              </div>
            </>
          );
        })
      )}
      <Spacing dir="v" size="2" />
      <Textarea
        id="comment"
        height="5rem"
        placeholder="댓글을 입력하세요."
        onChange={e => setComment(e.target.value)}
        value={comment}
      />
      <Spacing dir="v" size="0.5" />
      <Flex justifyContents="flexEnd">
        <div className={BUTTON}>
          <Btn type="button" variant="key" onClick={e => createComment(e)}>
            댓글 달기
          </Btn>
        </div>
      </Flex>
    </>
  );
}
// /schedule/{scheduleSeq}
