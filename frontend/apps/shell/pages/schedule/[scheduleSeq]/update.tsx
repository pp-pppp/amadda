import { auth, https } from '@amadda/fetch';
import { ScheduleEditFormData } from '@/components/schedule-edit/ScheduleEditForm/formdata';
import { responseToForm } from '@/utils/schedule/convertFormData';
import { ScheduleDetailReadResponse } from '@amadda/global-types';
import { GetServerSideProps, InferGetServerSidePropsType } from 'next';
import { ScheduleEditForm } from '@/components/schedule-edit/ScheduleEditForm/ScheduleEditForm';
import { ScheduleEditFrame } from '@/components/schedule-edit/ScheduleEditFrame/ScheduleEditFrame';
import { ErrorBoundary } from '@amadda/external-temporal';
import HeaderLayout from '@/components/layout/HeaderLayout';
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
  const scheduleSeq = context.params?.scheduleSeq;
  const token = context.req.cookies.Auth || '';
  const { data, code, message } = await https.get<ScheduleDetailReadResponse>(`${process.env.NEXT_PUBLIC_SCHEDULE}/api/schedule/${scheduleSeq}`, token);
  const scheduleDetail = responseToForm(data);
  return { props: { scheduleDetail } };
}) satisfies GetServerSideProps<{
  scheduleDetail: ScheduleEditFormData;
}>;
