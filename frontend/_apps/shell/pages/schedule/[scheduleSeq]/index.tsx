import { ErrorBoundary } from 'external-temporal';
import HeaderLayout from '@SH/components/HeaderLayout';
import dynamic from 'next/dynamic';
import Head from 'next/head';
import type { ReactNode } from 'react';

const Detail = dynamic(() => import('schedule/Detail'), { ssr: false });
export default function Page({ children }) {
  return (
    <div>
      <Head>
        <title>AMADDA</title>
        <meta property="og:title" content="AMADDA" key="title" />
      </Head>
      <HeaderLayout>
        <ErrorBoundary>
          <Detail />
        </ErrorBoundary>
      </HeaderLayout>
    </div>
  );
}
