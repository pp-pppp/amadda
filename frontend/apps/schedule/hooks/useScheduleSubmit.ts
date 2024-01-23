import { ScheduleCreateRequest, ScheduleUpdateRequest } from '@amadda/global-types';
import { NextRouter, useRouter } from 'next/router';
import { ScheduleEditFormProps } from '../components/ScheduleEdit/formdata';
import { clientFetch } from '@amadda/fetch';
import useSWR from 'swr';
import { formToRequest } from '@SCH/utils/convertFormData';

export function useScheduleSubmit() {
  const router = useRouter();

  async function submit(data: ScheduleEditFormProps) {
    const requestBody = formToRequest(data);
    if (router.basePath.includes('edit')) {
      const { scheduleSeq } = router.query;

      const { data, error } = useSWR(`${process.env.NEXT_PUBLIC_SCHEDULE}/api/schedule/${scheduleSeq}`, url =>
        clientFetch.put<ScheduleUpdateRequest, string>(url, requestBody as ScheduleUpdateRequest)
      );
      if (data) return router.push(`${process.env.NEXT_PUBLIC_SHELL}/schedule/${data}`);
      else return router.push(`${process.env.NEXT_PUBLIC_SHELL}/schedule`);
    } else {
      const { data, error } = useSWR(`${process.env.NEXT_PUBLIC_SCHEDULE}/api/schedule`, url =>
        clientFetch.post<ScheduleUpdateRequest, string>(url, requestBody as ScheduleUpdateRequest)
      );
      if (data) return router.push(`${process.env.NEXT_PUBLIC_SHELL}/schedule/${data}`);
      else return router.push(`${process.env.NEXT_PUBLIC_SHELL}/schedule`);
    }
  }

  return submit;
}
