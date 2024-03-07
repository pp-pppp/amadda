import { ErrorBoundary } from '@amadda/external-temporal';
import HeaderLayout from '@/components/layout/HeaderLayout';
import Head from 'next/head';
import { ScheduleEditFrame } from '@/components/schedule-edit/schedule-edit-frame/schedule-edit-frame';
import { ScheduleEditForm } from '@/components/schedule-edit/schedule-edit-form/schedule-edit-form';

export default function Page({ children }) {
  return (
    <div>
      <Head>
        <title>AMADDA</title>
        <meta property="og:title" content="AMADDA" key="title" />
      </Head>
      <HeaderLayout>
        <ErrorBoundary>
          <ScheduleEditFrame>
            <ScheduleEditForm />
          </ScheduleEditFrame>
        </ErrorBoundary>
      </HeaderLayout>
    </div>
  );
}
