import { MobileMain } from '@SCH/components/MobileMain/MobileMain';
import { ScheduleDetail } from '@SCH/components/ScheduleDetail/ScheduleDetail';
import { ScheduleDetailReadResponse } from '@amadda/global-types';
import { clientFetch } from '@amadda/fetch';
import type { InferGetServerSidePropsType, GetServerSideProps } from 'next';

export default function Page({ detail }: InferGetServerSidePropsType<typeof getServerSideProps>) {
  return <ScheduleDetail detail={detail} />;
}

export const getServerSideProps = (async context => {
  const scheduleSeq = context.params?.scheduleSeq;
  const detail = await clientFetch.get<ScheduleDetailReadResponse>(`${process.env.NEXT_PUBLIC_SCHEDULE}/api/schedule/${scheduleSeq}`);
  return { props: { detail } };
}) satisfies GetServerSideProps<{
  detail: ScheduleDetailReadResponse;
}>;
