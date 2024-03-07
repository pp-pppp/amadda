import React from 'react';
import { ErrorBoundary, Flex, H3, List, Spacing } from '@amadda/external-temporal';
import { useGetNotice } from '@/hooks/notice/useGetNotice';
import { NoticeToggle } from './notice-toggle-list';

export function NoticeConfigFrame() {
  const { noticeList, error } = useGetNotice();

  return (
    <div>
      <Flex flexDirection="column" justifyContents="start">
        <H3>알림 목록</H3>
        <Spacing dir="v" size="0.25" />
        <ErrorBoundary message="알람을 가져오는 데 실패했어요.">
          <List.Ul>
            {noticeList.map(n => (
              <List.Li key={n.alarmSeq + n.content}>
                <NoticeToggle {...n} />
              </List.Li>
            ))}
          </List.Ul>
        </ErrorBoundary>
      </Flex>
    </div>
  );
}
