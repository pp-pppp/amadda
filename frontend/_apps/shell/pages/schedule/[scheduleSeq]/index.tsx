import HeaderLayout from '@SH/components/HeaderLayout';
import Head from 'next/head';
import type { ReactNode } from 'react';

export default function Page({ children }) {
  return (
    <div>
      <Head>
        <title>AMADDA</title>
        <meta property="og:title" content="AMADDA" key="title" />
      </Head>
      <></>
    </div>
  );
}

Page.getLayout = function getLayout(page: ReactNode) {
  return <HeaderLayout>{page}</HeaderLayout>;
};
