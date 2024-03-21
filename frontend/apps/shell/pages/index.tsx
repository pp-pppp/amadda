import { Flex, Spacing, H3 } from '@amadda/external-temporal';

import { BASE } from '@/components/kakao-button/kakao-button.css';
import Head from 'next/head';
import Image from 'next/image';
import { MAIN } from '@/layout/layouts.css';
import { MainLayout } from '@/layout/main-layout';

const signIn = () => {
  window.location.href = `https://kauth.kakao.com/oauth/authorize?client_id=${process.env.NEXT_PUBLIC_KAKAO_KEY}&redirect_uri=${process.env.NEXT_PUBLIC_KAKAO_REDIRECT_URI}&response_type=code`;
};

export default function Page() {
  return (
    <div>
      <Head>
        <title>AMADDA</title>
        <meta property="og:title" content="AMADDA" key="title" />
      </Head>
      <MainLayout>
        <H3>AMADDA</H3>
        <Spacing size="5" />
        <img src="image/kakao_login.png" alt="카카오로 로그인하기" onClick={() => signIn()} className={BASE} />
      </MainLayout>
    </div>
  );
}
