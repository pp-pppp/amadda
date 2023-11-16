import { Suspense } from 'react';
import { NoticeConfigFrame } from '@N/components/NoticeConfig/NoticeConfigFrame';

export default function Page() {
  // const { data } = useNoticeConfig();
  return (
    <Suspense fallback={<>Loading</>}>
      <NoticeConfigFrame />
    </Suspense>
  );
}
