import { ScheduleEditForm } from '@SCH/components/ScheduleEdit/ScheduleEditForm/ScheduleEditForm';
import { ScheduleEditFrame } from '@SCH/components/ScheduleEdit/ScheduleEditFrame/ScheduleEditFrame';
import { ScheduleEditFormData } from '@SCH/components/ScheduleEdit/ScheduleEditForm/formdata';
import { responseToForm } from '@SCH/utils/convertFormData';
import { auth, https } from '@amadda/fetch';
import { ScheduleDetailReadResponse } from '@amadda/global-types';
import { GetServerSideProps, InferGetServerSidePropsType } from 'next';

export default function Page({ scheduleDetail }: InferGetServerSidePropsType<typeof getServerSideProps>) {
  return (
    <ScheduleEditFrame>
      <ScheduleEditForm scheduleDetail={scheduleDetail} />
    </ScheduleEditFrame>
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
