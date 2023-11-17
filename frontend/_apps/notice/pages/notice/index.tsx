import { NoticeFrame } from '@N/components/Notice/NoticeFrame';
import { Suspense } from 'react';

export default function Page() {
  return (
    <Suspense fallback={<>Loading</>}>
      <NoticeFrame />
    </Suspense>
  );
}
