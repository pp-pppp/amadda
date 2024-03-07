import React from 'react';
import { Flex, Spacing } from '@amadda/external-temporal';
import { BASE } from './Header.css';
import { Menu } from './Menu';

import { useRouter } from 'next/router';
import { useSubscribeNotice } from '@/hooks/notice/useSubscribeNotice';

export function Header() {
  const router = useRouter();
  const { data, error } = useSubscribeNotice();
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
        {router.basePath.includes('notice') || router.basePath.includes('friends') || (
          <Flex justifyContents="flexEnd">
            {/* <Menu
            iconType="search"
            onClick={() => {
              router.push(`${process.env.NEXT_PUBLIC_SHELL}/search`);
              일정 검색 페이지는 추후 구현 예정
            }}
          /> */}
            <Menu
              iconType={data ? 'noti' : 'noti_red'}
              onClick={() => {
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
