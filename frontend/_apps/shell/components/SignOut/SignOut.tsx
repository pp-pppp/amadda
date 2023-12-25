import React from 'react';
import { Btn, Flex, Spacing, Span } from 'external-temporal';
import { useRouter } from 'next/router';
import { KV } from 'connection';
import type { ApiResponse } from 'amadda-global-types';

export default function SignOut() {
  const router = useRouter();

  const signout = async () => {
    //현재 유저의 kakaoId 가져오기
    fetch(`${process.env.NEXT_PUBLIC_SHELL}/logout`)
      .then(res => res.json())
      .then(data =>
        fetch(`https://kapi.kakao.com/v1/user/logout`, {
          method: 'GET',
          headers: {
            Authorization: `Bearer ${KV.getToken(data)}`,
            'Content-type': 'applicatio n/x-www-form-urlencoded;charset=utf-8',
          },
        })
      )
      .catch(err => console.log(err));

    return router.push(`${process.env.NEXT_PUBLIC_SHELL}`);
  };

  return (
    <Flex justifyContents="center" alignItems="center" flexDirection="column">
      <Span>로그인이 만료되었어요.</Span>
      <Spacing dir="v" size="0.5" />
      <Span>다시 로그인 해주세요.</Span>
      <Spacing dir="v" size="2" />
      <Btn type="button" variant="key" disabled={false} onClick={() => signout()}>
        로그아웃
      </Btn>
    </Flex>
  );
}
