import React from 'react';
import { Flex, H6, P, Spacing, Switch } from 'external-temporal';
import ALARMTYPE_CONFIG_UI from '../../constants/ALARMTYPE_CONFIG_UI';

export interface NoticeProps {
  alarmSeq: number;
  content: string;
  isRead: boolean;
  isEnabled: boolean;
  alarmType:
    | 'FRIEND_REQUEST'
    | 'FRIEND_ACCEPT'
    | 'SCHEDULE_ASSIGNED'
    | 'MENTIONED'
    | 'SCHEDULE_UPDATE'
    | 'SCHEDULE_NOTI';
}

export function NoticeToggle({
  alarmSeq,
  content,
  isRead,
  isEnabled,
  alarmType,
}: NoticeProps) {
  return (
    <Flex justifyContents="spaceBetween">
      <Flex
        flexDirection="column"
        alignItems="start"
        justifyContents="spaceEvenly"
      >
        <H6 color="key">{content}</H6>
        <Spacing size="0.5" />
        <P type="caption" color="grey">
          {ALARMTYPE_CONFIG_UI[alarmType]}
        </P>
      </Flex>
      <Spacing dir="h" />
      <Switch id="3" selected={isEnabled} value="value" />
    </Flex>
  );
}
