import { auth, https } from '@amadda/fetch';
import { ScheduleEditFormData } from '@/components/schedule-edit/schedule-edit-form/formdata';
import { responseToForm } from '@/utils/schedule/convertFormData';
import { ScheduleDetailReadResponse } from '@amadda/global-types';
import { GetServerSideProps, InferGetServerSidePropsType } from 'next';
import { ScheduleEditForm } from '@/components/schedule-edit/schedule-edit-form/schedule-edit-form';
import { ScheduleEditFrame } from '@/components/schedule-edit/schedule-edit-frame/schedule-edit-frame';
import { ErrorBoundary } from '@amadda/external-temporal';
import HeaderLayout from '@/components/layout/header-layout';
import Head from 'next/head';

export default function Page({ scheduleDetail }: InferGetServerSidePropsType<typeof getServerSideProps>) {
  return (
    <div>
      <Head>
        <title>AMADDA</title>
        <meta property="og:title" content="AMADDA" key="title" />
      </Head>
      <HeaderLayout>
        <ErrorBoundary>
          <ScheduleEditFrame>
            <ScheduleEditForm scheduleDetail={scheduleDetail} />
          </ScheduleEditFrame>
        </ErrorBoundary>
      </HeaderLayout>
    </div>
  );
}

export const getServerSideProps = (async context => {
  const schedule_seq = context.params?.schedule_seq;
  const token = context.req.cookies.Auth || '';
  const { data, code, message } = await https.get<ScheduleDetailReadResponse>(`${process.env.NEXT_PUBLIC_SCHEDULE}/api/schedule/${schedule_seq}`, token);
  const scheduleDetail = responseToForm(data);
  return { props: { scheduleDetail } };
}) satisfies GetServerSideProps<{
  scheduleDetail: ScheduleEditFormData;
}>;
