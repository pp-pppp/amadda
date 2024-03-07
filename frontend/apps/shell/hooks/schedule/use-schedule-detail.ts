import { ScheduleDetailReadResponse } from '@amadda/global-types';
import { NextRouter } from 'next/router';
import useSWR from 'swr';
import { clientFetch } from '@amadda/fetch';

export function useScheduleDetail(router: NextRouter) {
  if (router.basePath.includes('edit')) {
    const { scheduleSeq } = router.query;
    const { data, error, isLoading } = useSWR(`${process.env.NEXT_PUBLIC_SCHEDULE}/api/schedule/${scheduleSeq}`, clientFetch.get<ScheduleDetailReadResponse>);
    return data;
  }
}
