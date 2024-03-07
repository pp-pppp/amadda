import { ErrorBoundary } from '@amadda/external-temporal';
import HeaderLayout from '@/components/layout/HeaderLayout';
import Head from 'next/head';
import { ScheduleDetail } from '@/components/schedule-detail/schedule-detail';
import { ScheduleDetailReadResponse } from '@amadda/global-types';
import { clientFetch } from '@amadda/fetch';
import type { InferGetServerSidePropsType, GetServerSideProps } from 'next';

export default function Page({ detail }: InferGetServerSidePropsType<typeof getServerSideProps>) {
  return (
    <div>
      <Head>
        <title>AMADDA</title>
        <meta property="og:title" content="AMADDA" key="title" />
      </Head>
      <HeaderLayout>
        <ErrorBoundary>
          <ScheduleDetail detail={detail} />;
        </ErrorBoundary>
      </HeaderLayout>
    </div>
  );
}

export const getServerSideProps = (async context => {
  const schedule_seq = context.params?.schedule_seq;
  const detail = await clientFetch.get<ScheduleDetailReadResponse>(`${process.env.NEXT_PUBLIC_SCHEDULE}/api/schedule/${schedule_seq}`);
  return { props: { detail } };
}) satisfies GetServerSideProps<{
  detail: ScheduleDetailReadResponse;
}>;
