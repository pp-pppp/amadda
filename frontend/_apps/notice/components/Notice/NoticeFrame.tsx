import React from 'react';
import { useEffect } from 'react';
import { Flex, H3, List, Spacing } from 'external-temporal';
import { AlarmReadResponse } from 'amadda-global-types';
import { useGetNotice } from '@N/hooks/useGetNotice';
import { useRouter } from 'next/router';
import { Notice } from './Notice';

export function NoticeFrame() {
  const { noticeList, setNoticeList, SWRerror } = useGetNotice();
  const router = useRouter();
  const basePath = router.asPath;
  useEffect(() => {
    const eventSource = new EventSource(`${process.env.NEXT_PUBLIC_NOTICE}/api/event`);

    eventSource.onmessage = async (e: MessageEvent) => {
      const data: AlarmReadResponse = JSON.parse(await e.data);
      console.log('Received data:', data);
      setNoticeList([...noticeList, data]);
    };

    eventSource.onerror = async (error: Event) => {
      console.error('EventSource failed:', error);
      eventSource.close();
    };

    return () => {
      eventSource.close();
    };
  }, [basePath]);
  return (
    <div>
      <H3>알림 목록</H3>
      <Flex flexDirection="column" justifyContents="start" alignItems="center">
        <Spacing dir="v" size="0.25" />
        <List.Ul>
          {noticeList.map(n => (
            <List.Li key={n.alarmSeq + n.content}>
              <Notice {...n} />
            </List.Li>
          ))}
        </List.Ul>
      </Flex>
    </div>
  );
}
