import React from 'react';
import { useEffect, useState } from 'react';
import type { ReactNode } from 'react';
import { Flex, H3, List, Spacing } from 'external-temporal';
import { AlarmReadResponse } from 'amadda-global-types';
import { useGetNotice } from '@N/hooks/useGetNotice';
import { useRouter } from 'next/router';
import { NoticeToggle } from './NoticeToggle';

export function NoticeConfigFrame() {
  const { noticeList, setNoticeList, SWRerror } = useGetNotice();

  return (
    <Flex flexDirection="column" justifyContents="start">
      <H3>알림 목록</H3>
      <Spacing dir="v" size="0.25" />
      <List.Ul>
        {noticeList.map(n => (
          <List.Li key={n.alarmSeq + n.content}>
            <NoticeToggle {...n} />
          </List.Li>
        ))}
      </List.Ul>
    </Flex>
  );
}
