import HeaderLayout from '@SH/components/HeaderLayout';
import dynamic from 'next/dynamic';
import Head from 'next/head';
import type { ReactNode } from 'react';

const NoticeConfig = dynamic(() => import('notice/NoticeConfig'), {
  ssr: false,
});

export default function Page({ children }) {
  return (
    <div>
      <Head>
        <title>AMADDA</title>
        <meta property="og:title" content="AMADDA" key="title" />
      </Head>
      <HeaderLayout>
        <NoticeConfig />
      </HeaderLayout>
    </div>
  );
}
