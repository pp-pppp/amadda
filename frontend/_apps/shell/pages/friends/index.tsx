import { ErrorBoundary } from 'external-temporal';
import HeaderLayout from '@SH/components/HeaderLayout';
import dynamic from 'next/dynamic';
import Head from 'next/head';

const Friend = dynamic(() => import('user/Friend'), { ssr: false });

export default function Page() {
  return (
    <div>
      <Head>
        <title>AMADDA</title>
        <meta property="og:title" content="AMADDA" key="title" />
      </Head>
      <HeaderLayout>
        <ErrorBoundary>
          <Friend />
        </ErrorBoundary>
      </HeaderLayout>
    </div>
  );
}
