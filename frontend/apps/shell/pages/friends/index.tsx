import { ErrorBoundary } from '@amadda/external-temporal';
import HeaderLayout from '@/components/layout/HeaderLayout';
import Head from 'next/head';
import FriendPage from '@/components/friend-page/friend-page';

export default function Page() {
  return (
    <div>
      <Head>
        <title>AMADDA</title>
        <meta property="og:title" content="AMADDA" key="title" />
      </Head>
      <HeaderLayout>
        <ErrorBoundary>
          <FriendPage />
        </ErrorBoundary>
      </HeaderLayout>
    </div>
  );
}
