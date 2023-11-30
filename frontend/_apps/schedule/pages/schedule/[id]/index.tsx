import { MobileMain } from '@SCH/components/MobileMain/MobileMain';
import { ScheduleDetail } from '@SCH/components/ScheduleDetail/ScheduleDetail';
import { ScheduleDetailReadResponse } from 'amadda-global-types';
import { http } from 'connection';
import type { InferGetServerSidePropsType, GetServerSideProps } from 'next';

export default function Page({ detail }: InferGetServerSidePropsType<typeof getServerSideProps>) {
  return <ScheduleDetail detail={detail} />;
}

export const getServerSideProps = (async context => {
  const scheduleSeq = context.params?.id;
  const res = await http.get<ScheduleDetailReadResponse>(`${process.env.NEXT_PUBLIC_SCHEDULE}/api/schedule/${scheduleSeq}`);
  const detail = res.data;
  return { props: { detail } };
}) satisfies GetServerSideProps<{
  detail: ScheduleDetailReadResponse;
}>;
