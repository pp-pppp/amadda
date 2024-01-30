import { ScheduleUpdateRequest } from '@amadda/global-types';
import { useRouter } from 'next/router';
import { ScheduleEditFormData } from '../components/ScheduleEdit/formdata';
import { clientFetch } from '@amadda/fetch';
import { formToRequest } from '@SCH/utils/convertFormData';

export function useScheduleSubmit() {
  const router = useRouter();

  async function submit(data: ScheduleEditFormData) {
    const requestBody = formToRequest(data);
    if (router.basePath.includes('update')) {
      const { scheduleSeq } = router.query;
      const data = clientFetch.put<ScheduleUpdateRequest, string>(
        `${process.env.NEXT_PUBLIC_SCHEDULE}/api/schedule/${scheduleSeq}`,
        requestBody as ScheduleUpdateRequest
      );
      if (data) return router.push(`${process.env.NEXT_PUBLIC_SHELL}/schedule/${data}`);
      else return router.push(`${process.env.NEXT_PUBLIC_SHELL}/schedule`);
    } else {
      const data = clientFetch.post<ScheduleUpdateRequest, string>(`${process.env.NEXT_PUBLIC_SCHEDULE}/api/schedule`, requestBody as ScheduleUpdateRequest);
      if (data) return router.push(`${process.env.NEXT_PUBLIC_SHELL}/schedule/${data}`);
      else return router.push(`${process.env.NEXT_PUBLIC_SHELL}/schedule`);
    }
  }

  return submit;
}
