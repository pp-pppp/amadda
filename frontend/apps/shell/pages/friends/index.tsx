import { ErrorBoundary } from '@amadda/external-temporal';
import HeaderLayout from '@/components/layout/header-layout';
import Head from 'next/head';
import { FriendFrameLayout } from '@/components/friend-group/friend-group-layout/friend-group-layout';
import { FriendRouter } from '@/components/friend-frame/friend-router';

export default function Page() {
  return (
    <div>
      <Head>
        <title>AMADDA</title>
        <meta property="og:title" content="AMADDA" key="title" />
      </Head>
      <HeaderLayout>
        <ErrorBoundary>
          <FriendFrameLayout>
            <FriendRouter />
          </FriendFrameLayout>
        </ErrorBoundary>
      </HeaderLayout>
    </div>
  );
}
