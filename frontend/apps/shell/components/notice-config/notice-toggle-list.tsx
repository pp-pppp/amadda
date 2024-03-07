import React, { useState } from 'react';
import { Flex, H6, P, Spacing, Switch } from '@amadda/external-temporal';
import ALARMTYPE from '@/constants/notice/ALARMTYPE';
import { BACKGROUND } from '../notice-view/notice-list.css';
import { clientFetch } from '@amadda/fetch';

export interface NoticeProps {
  alarmSeq?: number | null;
  content: string;
  isRead: boolean;
  isEnabled: boolean;
  alarmType: keyof typeof ALARMTYPE;
}

export function NoticeToggle({ alarmSeq = null, content, isRead, isEnabled, alarmType }: NoticeProps) {
  const [sub, setSub] = useState<boolean>(isEnabled);
  const handleSubscribe = async () => {
    isEnabled
      ? (await clientFetch.post(`${process.env.NEXT_PUBLIC_NOTICE}/api/alarm/unsubscribe`, {
          alarmType,
        })) && setSub(!sub)
      : (await clientFetch.post(`${process.env.NEXT_PUBLIC_NOTICE}/api/alarm/subscribe`, {
          alarmType,
        })) && setSub(!sub);
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
