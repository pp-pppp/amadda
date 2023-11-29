import { NoticeConfigFrame } from '@N/components/NoticeConfig/NoticeConfigFrame';
import ErrorBoundary from '#/components/fallback/ErrorBoundary/ErrorBoundary';

export default function Page() {
  return (
    <ErrorBoundary>
      <NoticeConfigFrame />
    </ErrorBoundary>
  );
}
