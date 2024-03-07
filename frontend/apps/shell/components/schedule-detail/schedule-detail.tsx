import React, { useState } from 'react';
import { CommentCreateRequest, ScheduleDetailReadResponse } from '@amadda/global-types';
import { Chip, Flex, H2, P, Icon, Spacing, Span, Profile, Input, Btn, Textarea } from '@amadda/external-temporal';
import { InferGetServerSidePropsType } from 'next';
import { getServerSideProps } from '@/pages/schedule/[schedule_seq]';
import CALENDAR from '@/constants/schedule/CALENDAR';
import { BASE, BUTTON, GRID } from './schedule-detail.css';
import { CategoryOption } from '../category-select/category-option';
import { clientFetch } from '@amadda/fetch';

export function ScheduleDetail({ detail }: InferGetServerSidePropsType<typeof getServerSideProps>) {
  const [comment, setComment] = useState('');

  const createComment = async e => {
    const CommentCreateRequestBody: CommentCreateRequest = {
      commentContent: comment,
    };

    await clientFetch.post(`${process.env.PUBLIC_NEXT_SCHEDULE}/api/schedule/${detail.scheduleSeq}/comment`, CommentCreateRequestBody);
    setComment('');
  };

  return (
    <>
      <H2>{detail.scheduleName}</H2>
      <Spacing dir="v" size="2" />
      <Span>참가자</Span>
      <Spacing dir="v" size="0.5" />
      {detail.participants.map((participant, idx) => (
        <Chip key={idx} type="suggestion" label={participant.userName} shape="square" />
      ))}
      <Spacing dir="v" size="2" />
      <Span>날짜</Span>
      <Spacing dir="v" size="0.5" />
      {!detail.isDateSelected ? (
        <Chip type="suggestion" label={CALENDAR.NO_DATE} shape="square" />
      ) : (
        <div className={BASE}>
          <Span>
            {detail.scheduleStartAt.split(' ')[0].split('-')[0]}년 {detail.scheduleStartAt.split(' ')[0].split('-')[1]}월{' '}
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
              {detail.scheduleStartAt.split(' ')[1].split('-')[0]}시 {detail.scheduleStartAt.split(' ')[1].split('-')[1]}분{' '}
            </Span>
          </div>
          <Spacing dir="h" size="0.5" />
          <Span>~</Span>
          <Spacing dir="h" size="0.5" />
          <div className={BASE}>
            <Span>
              {detail.scheduleEndAt.split(' ')[1].split('-')[0]}시 {detail.scheduleEndAt.split(' ')[1].split('-')[1]}분{' '}
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
      <CategoryOption category={detail.category} />
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
                <Profile src={comment.user.imageUrl} alt={comment.user.userSeq.toString()} />
                <Flex justifyContents="start" alignItems="start" flexDirection="column">
                  <Span>{comment.user.userName}</Span>
                </Flex>
                <Span>{comment.commentContent}</Span>
              </div>
            </>
          );
        })
      )}
      <Spacing dir="v" size="2" />
      <Textarea name="comment" id="comment" height="5rem" placeholder="댓글을 입력하세요." onChange={e => setComment(e.target.value)} value={comment} />
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
