import React from 'react';
import { useEffect, useState } from 'react';
import { Flex, Spacing } from 'external-temporal';
import { BASE } from './Header.css';
import { Menu } from './Menu';
import type { AlarmReadResponse } from 'amadda-global-types';
import { useRouter } from 'next/router';

export function Header() {
  const [notice, setNotice] = useState(false);
  const router = useRouter();

  useEffect(() => {
    const eventSource = new EventSource(
      `${process.env.NEXT_PUBLIC_NOTICE}/api/event`
    );

    eventSource.onmessage = async (e: MessageEvent) => {
      const data: AlarmReadResponse = JSON.parse(await e.data);
      if (!data?.isRead) setNotice(true);
    };

    eventSource.onerror = async (error: Event) => {
      setNotice(false);
      console.error('EventSource failed:', error);
      eventSource.close();
    };

    return () => {
      eventSource.close();
    };
  }, [router.basePath]);
  return (
    <div className={BASE}>
      <Flex justifyContents="spaceBetween">
        <Flex justifyContents="start">
          <Menu
            iconType="cal"
            onClick={() => {
              router.push(`${process.env.NEXT_PUBLIC_SHELL}/schedule`);
            }}
          />
        </Flex>
        {router.basePath.includes('notice') ||
          router.basePath.includes('friends') || (
            <Flex justifyContents="flexEnd">
              {/* <Menu
            iconType="search"
            onClick={() => {
              router.push(`${process.env.NEXT_PUBLIC_SHELL}/search`);
              일정 검색 페이지는 추후 구현 예정
            }}
          /> */}
              <Menu
                iconType={notice ? 'noti' : 'noti_red'}
                onClick={() => {
                  setNotice(false);
                  router.push(`${process.env.NEXT_PUBLIC_SHELL}/notice`);
                }}
              />
              <Spacing size="0.25" />
              <Menu
                iconType="friends"
                onClick={() => {
                  router.push(`${process.env.NEXT_PUBLIC_SHELL}/friends`);
                }}
              />
              <Spacing size="0.25" />
              <Menu
                iconType="config"
                onClick={() => {
                  router.push(`${process.env.NEXT_PUBLIC_SHELL}/notice/config`);
                }}
              />
            </Flex>
          )}
      </Flex>
    </div>
  );
}
