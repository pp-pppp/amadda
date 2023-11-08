import React from 'react';
import { Flex, H6, P, Spacing, Switch } from 'external-temporal';
import ALARMTYPE from '../../constants/ALARMTYPE';
import { BACKGROUND } from './Notice.css';

export interface NoticeProps {
  alarmSeq: number;
  content: string;
  isRead: boolean;
  isEnabled: boolean;
  alarmType: keyof typeof ALARMTYPE;
}

export function NoticeToggle({
  alarmSeq,
  content,
  isRead,
  isEnabled,
  alarmType,
}: NoticeProps) {
  return (
    <div className={BACKGROUND.normal}>
      <Flex justifyContents="spaceBetween">
        <Flex
          flexDirection="column"
          alignItems="start"
          justifyContents="spaceEvenly"
        >
          <H6 color="key">{content}</H6>
          <Spacing size="0.5" />
          <P type="caption" color="grey">
            {ALARMTYPE[alarmType]}
          </P>
        </Flex>
        <Spacing dir="h" />
        {alarmType !== 'SCHEDULE_NOTI' && (
          <Switch
            id={String(alarmSeq)}
            selected={isEnabled}
            onToggle={() => isEnabled}
          />
        )}
      </Flex>
    </div>
  );
}
