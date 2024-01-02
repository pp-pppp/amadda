import { NoticeFrame } from '@N/components/Notice/NoticeFrame';
import { ErrorBoundary } from 'external-temporal';

export default function Page() {
  return (
    <ErrorBoundary>
      <NoticeFrame />
    </ErrorBoundary>
  );
}
