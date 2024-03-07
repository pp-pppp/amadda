import HeaderLayout from '@/components/layout/HeaderLayout';
import { NoticeFrame } from '@/components/Notice/NoticeFrame';
import { NoticeConfigFrame } from '@/components/notice-config/NoticeConfigFrame';
import { ErrorBoundary } from '@amadda/external-temporal';

import dynamic from 'next/dynamic';
import Head from 'next/head';

export default function Page({ children }) {
  return (
    <div>
      <Head>
        <title>AMADDA</title>
        <meta property="og:title" content="AMADDA" key="title" />
      </Head>
      <HeaderLayout>
        <ErrorBoundary>
          <NoticeConfigFrame />
        </ErrorBoundary>
      </HeaderLayout>
    </div>
  );
}
