import React from 'react';
import { useEffect } from 'react';
import { ErrorBoundary, Flex, H3, List, Spacing } from '@amadda/external-temporal';
import { AlarmReadResponse } from '@amadda/global-types';
import { useGetNotice } from '@/hooks/notice/useGetNotice';
import { useRouter } from 'next/router';
import { Notice } from './notice-list';

export function NoticeFrame() {
  const { noticeList, error } = useGetNotice();

  return (
    <div>
      <H3>알림 목록</H3>
      <Flex flexDirection="column" justifyContents="start" alignItems="center">
        <Spacing dir="v" size="0.25" />
        <ErrorBoundary message="알람을 가져오는 데 실패했어요.">
          <List.Ul>
            {noticeList.map(n => (
              <List.Li key={n.alarmSeq + n.content}>
                <Notice {...n} />
              </List.Li>
            ))}
          </List.Ul>
        </ErrorBoundary>
      </Flex>
    </div>
  );
}
