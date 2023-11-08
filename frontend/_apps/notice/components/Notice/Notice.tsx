import React from 'react';
import { BtnRound, Flex, H4, H5, Spacing } from 'external-temporal';

export interface NoticeProps {
  alarmSeq: number;
  content: string;
  isRead: boolean;
  alarmType:
    | 'FRIEND_REQUEST'
    | 'FRIEND_ACCEPT'
    | 'SCHEDULE_ASSIGNED'
    | 'MENTIONED'
    | 'SCHEDULE_UPDATE'
    | 'SCHEDULE_NOTI';
}

export function Notice({ content, alarmType }: NoticeProps) {
  //알림 타입
  //프로필 사진 (유저 정보)
  //알림 내용
  //친구신청일 경우 수락, 거절 버튼
  //읽었으면 사라져야하는데...
  //링크 있어야함. 해당 글로 갈 수 있는 링크
  return (
    <Flex justifyContents="spaceBetween">
      <H5>{content}</H5>
      <Spacing dir="h" />
      {alarmType === 'FRIEND_REQUEST' && (
        <Flex justifyContents="start">
          <BtnRound
            type="button"
            variant="key"
            onClick={() => {}}
            disabled={false}
          >
            수락
          </BtnRound>
          <Spacing dir="h" size="0.5" />
          <BtnRound
            type="button"
            variant="white"
            onClick={() => {}}
            disabled={false}
          >
            거절
          </BtnRound>
        </Flex>
      )}
    </Flex>
  );
}
