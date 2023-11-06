import { IndexLayout } from '@/layout/IndexLayout';
import { H1, Flex, Spacing } from 'external-temporal';
import { signIn } from 'next-auth/react';

export default function Page() {
  return (
    <IndexLayout>
      <Flex justifyContents="center" alignItems="center" flexDirection="column">
        <H1>Amadda</H1>
        <Spacing size="5" />
        <img
          src="image/kakao_login.png"
          onClick={() => signIn('kakao', { callbackUrl: '/main' })}
        />
      </Flex>
    </IndexLayout>
  );
}
