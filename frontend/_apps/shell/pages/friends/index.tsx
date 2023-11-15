import dynamic from 'next/dynamic';
import { Suspense } from 'react';

const Friend = dynamic(() => import('user/Friend'), { ssr: false });

export default function Page() {
  return (
    <>
      <Suspense fallback={<>loading</>}>
        <Friend />
      </Suspense>
    </>
  );
}
