import { ScheduleCreateRequest, ScheduleUpdateRequest } from '@amadda/global-types';
import { useRouter } from 'next/router';
import { ScheduleEditFormProps } from '../components/ScheduleEdit/formdata';

export async function useScheduleSubmit(data: ScheduleEditFormProps) {
  const router = useRouter();

  let request: ScheduleCreateRequest | ScheduleUpdateRequest;

  if (router.basePath.includes('edit')) {
    request = convert(data, 'PUT');
    const { scheduleSeq } = router.query;

    return fetch(`${process.env.NEXT_PUBLIC_SCHEDULE}/api/schedule/${scheduleSeq}`, {
      method: 'PUT',
      body: JSON.stringify(data),
      headers: {
        'Content-Type': 'application/json',
      },
    })
      .then(res => res.json())
      .then(json => router.push(`${process.env.NEXT_PUBLIC_SHELL}/schedule/${json.data}`))
      .catch(err => router.push(`${process.env.NEXT_PUBLIC_SHELL}/schedule`));
  }
  request = convert(data, 'POST');
  return fetch(`${process.env.NEXT_PUBLIC_SCHEDULE}/api/schedule`, {
    method: 'POST',
    body: JSON.stringify(data),
    headers: {
      'Content-Type': 'application/json',
    },
  })
    .then(res => res.json())
    .then(json => router.push(`${process.env.NEXT_PUBLIC_SHELL}/schedule/${json.data}`))
    .catch(err => router.push(`${process.env.NEXT_PUBLIC_SHELL}/schedule`));
}

function convert(data: ScheduleEditFormProps, type: 'POST' | 'PUT'): ScheduleCreateRequest | ScheduleUpdateRequest {
  const result = Object.assign(data);

  return result;
}
