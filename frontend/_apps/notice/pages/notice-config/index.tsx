import { NoticeConfigFrame } from '@N/components/NoticeConfig/NoticeConfigFrame';
import { ErrorBoundary } from 'external-temporal';

export default function Page() {
  return (
    <ErrorBoundary>
      <NoticeConfigFrame />
    </ErrorBoundary>
  );
}
