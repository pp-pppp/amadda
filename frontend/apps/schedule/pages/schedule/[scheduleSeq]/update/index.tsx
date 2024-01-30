import { ScheduleEdit } from '@SCH/components/ScheduleEdit/ScheduleEdit';
import { ScheduleEditFormData } from '@SCH/components/ScheduleEdit/formdata';
import { responseToForm } from '@SCH/utils/convertFormData';
import { auth, https } from '@amadda/fetch';
import { ScheduleDetailReadResponse } from '@amadda/global-types';
import { GetServerSideProps, InferGetServerSidePropsType } from 'next';

export default function Page({ scheduleDetail }: InferGetServerSidePropsType<typeof getServerSideProps>) {
  return <ScheduleEdit scheduleDetail={scheduleDetail} />;
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
