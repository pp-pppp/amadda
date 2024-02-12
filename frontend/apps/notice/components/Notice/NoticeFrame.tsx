import React, { Suspense } from 'react';
import { ErrorBoundary, Flex, H3, List, Loading, Spacing } from '@amadda/external-temporal';
import { useGetNotice } from '@N/hooks/useGetNotice';
import { Notice } from './Notice';

export function NoticeFrame() {
  const { noticeList } = useGetNotice();

  return (
    <div>
      <H3>알림 목록</H3>
      <Flex flexDirection="column" justifyContents="start" alignItems="center">
        <Spacing dir="v" size="0.25" />
        <ErrorBoundary message="알람을 가져오는 데 실패했어요.">
          <List.Ul>
            <Suspense fallback={<Loading />}>
              {noticeList &&
                noticeList.map(n => (
                  <List.Li key={n.alarmSeq + n.content}>
                    <Notice {...n} />
                  </List.Li>
                ))}
            </Suspense>
          </List.Ul>
        </ErrorBoundary>
      </Flex>
    </div>
  );
}
