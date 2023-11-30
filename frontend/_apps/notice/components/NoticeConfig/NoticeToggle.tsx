import React, { useState } from 'react';
import { Flex, H6, P, Spacing, Switch } from 'external-temporal';
import ALARMTYPE from '../../constants/ALARMTYPE';
import { BACKGROUND } from '../Notice/Notice.css';
import { http } from '@N/utils/http';

export interface NoticeProps {
  alarmSeq?: number | null;
  content: string;
  isRead: boolean;
  isEnabled: boolean;
  alarmType: keyof typeof ALARMTYPE;
}

export function NoticeToggle({ alarmSeq = null, content, isRead, isEnabled, alarmType }: NoticeProps) {
  const [sub, setSub] = useState<boolean>(isEnabled);
  const handleSubscribe = () => {
    isEnabled
      ? http
          .post(`${process.env.NEXT_PUBLIC_NOTICE}/api/alarm/unsubscribe`, {
            alarmType,
          })
          .then(res => {
            res.status < 300 && setSub(!sub);
          })
          .catch(err => {})
      : http
          .post(`${process.env.NEXT_PUBLIC_NOTICE}/api/alarm/subscribe`, {
            alarmType,
          })
          .then(res => {
            res.status < 300 && setSub(!sub);
          })
          .catch(err => {});
  };
  return (
    <div className={BACKGROUND.normal}>
      <Flex justifyContents="spaceBetween">
        <Flex flexDirection="column" alignItems="start" justifyContents="spaceEvenly">
          <H6 color="key">{content}</H6>
          <Spacing size="0.5" />
          <P type="caption" color="grey">
            {ALARMTYPE[alarmType]}
          </P>
        </Flex>
        <Spacing dir="h" />
        {alarmType !== 'SCHEDULE_NOTI' && <Switch id={String(alarmSeq) + content} selected={sub} onToggle={() => handleSubscribe()} />}
      </Flex>
    </div>
  );
}
