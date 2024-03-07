import { ErrorBoundary } from '@amadda/external-temporal';
import HeaderLayout from '@/components/layout/HeaderLayout';
import Head from 'next/head';
import { ScheduleDetail } from '@/components/schedule-detail/ScheduleDetail';
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
  const scheduleSeq = context.params?.scheduleSeq;
  const detail = await clientFetch.get<ScheduleDetailReadResponse>(`${process.env.NEXT_PUBLIC_SCHEDULE}/api/schedule/${scheduleSeq}`);
  return { props: { detail } };
}) satisfies GetServerSideProps<{
  detail: ScheduleDetailReadResponse;
}>;
