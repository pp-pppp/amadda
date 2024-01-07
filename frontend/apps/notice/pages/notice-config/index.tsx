import { NoticeConfigFrame } from '@N/components/NoticeConfig/NoticeConfigFrame';
import { ErrorBoundary } from '@amadda/external-temporal';

export default function Page() {
  return (
    <ErrorBoundary>
      <NoticeConfigFrame />
    </ErrorBoundary>
  );
}
