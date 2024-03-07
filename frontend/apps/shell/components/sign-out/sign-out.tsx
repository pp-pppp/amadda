import React from 'react';
import { Btn, Flex, Spacing, Span } from '@amadda/external-temporal';
import { useRouter } from 'next/router';
import { clientFetch, kakaoAuth, kv } from '@amadda/fetch';
import * as Sentry from '@sentry/nextjs';

export default function SignOut() {
  const router = useRouter();

  const signout = async () => {
    try {
      const kakaoId = await clientFetch.get<string>(`${process.env.NEXT_PUBLIC_SHELL}/api/user/logout`);
      const kakaoToken = await kv.getToken(kakaoId);
      await kakaoAuth.secureGet(`https://kapi.kakao.com/v1/user/logout`, kakaoToken);

      router.push(`${process.env.NEXT_PUBLIC_SHELL}`);
    } catch (err) {
      Sentry.captureException(err);
    }
  };

  return (
    <Flex justifyContents="center" alignItems="center" flexDirection="column">
      <Span>로그인이 만료되었어요.</Span>
      <Spacing dir="v" size="0.5" />
      <Span>다시 로그인 해주세요.</Span>
      <Spacing dir="v" size="2" />
      <Btn type="button" variant="key" disabled={false} onClick={async () => await signout()}>
        로그아웃
      </Btn>
    </Flex>
  );
}
