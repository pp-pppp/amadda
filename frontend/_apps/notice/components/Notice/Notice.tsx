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
