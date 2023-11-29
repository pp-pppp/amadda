import ErrorBoundary from '#/components/fallback/ErrorBoundary/ErrorBoundary';
import { NoticeFrame } from '@N/components/Notice/NoticeFrame';

export default function Page() {
  return (
    <ErrorBoundary>
      <NoticeFrame />
    </ErrorBoundary>
  );
}
