import React from 'react';
import { BtnRound, Flex, H6, P, Spacing } from 'external-temporal';
import ALARMTYPE from '../../constants/ALARMTYPE';
import { BACKGROUND } from './Notice.css';
import ALARMUI from '../../constants/ALARMUI';

export interface NoticeProps {
  alarmSeq?: number;
  content: string;
  isRead: boolean;
  alarmType: keyof typeof ALARMTYPE;
}

export function Notice({ content, alarmType, isRead }: NoticeProps) {
  const className = isRead ? 'isRead' : 'notRead';
  return (
    <div className={BACKGROUND[className]}>
      <Flex justifyContents="spaceBetween">
        <Flex
          flexDirection="column"
          justifyContents="center"
          alignItems="start"
        >
          <H6>{content}</H6>
          {alarmType === 'FRIEND_REQUEST' && (
            <>
              <Spacing size="0.5" />
              <P color="grey" type="caption">
                {ALARMUI.Notice.FRIEND_REQUEST_CAPTION}
              </P>
            </>
          )}
        </Flex>
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
    </div>
  );
}
