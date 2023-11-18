import { Flex, Spacing, H2 } from 'external-temporal';
import { signIn } from 'next-auth/react';
import { BASE } from '@SH/components/KakaoBtn/KakaoBtn.css';
import Head from 'next/head';

export default function Page() {
  return (
    <div>
      <Head>
        <title>AMADDA</title>
        <meta property="og:title" content="AMADDA" key="title" />
      </Head>
      <Flex justifyContents="center" alignItems="center" flexDirection="column">
        <H2>Amadda</H2>
        <Spacing size="5" />
        <img
          src="image/kakao_login.png"
          alt="카카오로 로그인하기"
          onClick={() => signIn('kakao', { callbackUrl: '/main' })}
          className={BASE}
        />
      </Flex>
    </div>
  );
}
