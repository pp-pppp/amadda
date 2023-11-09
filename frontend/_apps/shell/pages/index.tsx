import { H1, Flex, Spacing } from 'external-temporal';
import { signIn } from 'next-auth/react';
import { IndexLayout } from '@/layout/IndexLayout';
import { BASE } from '@/components/KakaoBtn/KakaoBtn.css';

export default function Page() {
  return (
    <IndexLayout>
      <Flex justifyContents="center" alignItems="center" flexDirection="column">
        <H1>Amadda</H1>
        <Spacing size="5" />
        <img
          src="image/kakao_login.png"
          alt="카카오로 로그인하기"
          onClick={() => signIn('kakao', { callbackUrl: '/main' })}
          className={BASE}
        />
      </Flex>
    </IndexLayout>
  );
}
