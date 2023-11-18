import { Loading } from 'external-temporal';
import dynamic from 'next/dynamic';
import Head from 'next/head';
import { Suspense } from 'react';

const Friend = dynamic(() => import('user/Friend'), { ssr: false });

export default function Page() {
  return (
    <div>
      <Head>
        <title>AMADDA</title>
        <meta property="og:title" content="AMADDA" key="title" />
      </Head>
      <Suspense fallback={<Loading />}>
        <Friend />
      </Suspense>
    </div>
  );
}
