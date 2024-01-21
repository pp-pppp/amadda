import { ScheduleCreateRequest, ScheduleUpdateRequest } from '@amadda/global-types';
import { useRouter } from 'next/router';
import { ScheduleEditFormProps } from '../components/ScheduleEdit/formdata';
import { clientFetch } from '@amadda/fetch';
import useSWR from 'swr';

export async function useScheduleSubmit(data: ScheduleEditFormProps) {
  const router = useRouter();

  const request: ScheduleCreateRequest | ScheduleUpdateRequest = convert(data);

  if (router.basePath.includes('edit')) {
    const { scheduleSeq } = router.query;

    const { data, error } = useSWR(`${process.env.NEXT_PUBLIC_SCHEDULE}/api/schedule/${scheduleSeq}`, url =>
      clientFetch.put<ScheduleUpdateRequest, string>(url, request as ScheduleUpdateRequest)
    );
    if (error) return router.push(`${process.env.NEXT_PUBLIC_SHELL}/schedule`);
    if (data) return router.push(`${process.env.NEXT_PUBLIC_SHELL}/schedule/${data}`);
  } else {
    const { data, error } = useSWR(`${process.env.NEXT_PUBLIC_SCHEDULE}/api/schedule`, url =>
      clientFetch.post<ScheduleUpdateRequest, string>(url, request as ScheduleUpdateRequest)
    );
    if (error) return router.push(`${process.env.NEXT_PUBLIC_SHELL}/schedule`);
    if (data) return router.push(`${process.env.NEXT_PUBLIC_SHELL}/schedule/${data}`);
  }
}

function convert(formData: ScheduleEditFormProps): ScheduleCreateRequest | ScheduleUpdateRequest {
  return formData.categorySeq
    ? {
        scheduleContent: formData.scheduleContent,
        isTimeSelected: formData.isTimeSelected,
        isDateSelected: formData.isDateSelected,
        isAllday: formData.isAllday,
        isAuthorizedAll: formData.isAllday,
        scheduleStartAt: `${formData.startYear}-${formData.startMonth}-${formData.startDate} ${formData.startTime}:${formData.startMinute}`,
        scheduleEndAt: `${formData.endYear}-${formData.endMonth}-${formData.endDate} ${formData.endTime}:${formData.endMinute}`,
        participants: formData.participants,
        alarmTime: String(formData.alarmTime),
        scheduleName: formData.scheduleName,
        scheduleMemo: formData.scheduleMemo,
        categorySeq: Number(formData.categorySeq),
      }
    : {
        scheduleContent: formData.scheduleContent,
        isTimeSelected: formData.isTimeSelected,
        isDateSelected: formData.isDateSelected,
        isAllday: formData.isAllday,
        isAuthorizedAll: formData.isAllday,
        scheduleStartAt: `${formData.startYear}-${formData.startMonth}-${formData.startDate} ${formData.startTime}:${formData.startMinute}`,
        scheduleEndAt: `${formData.endYear}-${formData.endMonth}-${formData.endDate} ${formData.endTime}:${formData.endMinute}`,
        participants: formData.participants,
        alarmTime: String(formData.alarmTime),
        scheduleName: formData.scheduleName,
        scheduleMemo: formData.scheduleMemo,
      };
}
