import HeaderLayout from '@SH/components/HeaderLayout';
import dynamic from 'next/dynamic';
import Head from 'next/head';
import type { ReactNode } from 'react';

const Calendar = dynamic(() => import('schedule/Calendar'), { ssr: false });
export default function Page({ children }) {
  return (
    <div>
      <Head>
        <title>AMADDA</title>
        <meta property="og:title" content="AMADDA" key="title" />
      </Head>
      <HeaderLayout>
        <Calendar />
      </HeaderLayout>
    </div>
  );
}

Page.getLayout = function getLayout(page: ReactNode) {
  return <HeaderLayout>{page}</HeaderLayout>;
};
