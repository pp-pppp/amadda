import { clientFetch } from '@amadda/fetch';
import { AlarmReadResponse } from '@amadda/global-types';
import { useEffect, useState } from 'react';
import useSWR from 'swr';
import useSWRSubscription from 'swr/subscription';

const fetcher = () => clientFetch.get<Array<AlarmReadResponse>>(`${process.env.NEXT_PUBLIC_NOTICE}/api/alarm`);

export function useGetNotice() {
  const [noticeList, setNoticeList] = useState<Array<AlarmReadResponse>>([]);
  const { data, error } = useSWR<Array<AlarmReadResponse>>('/api/alarm', fetcher);

  const { data: sub, error: eventSrcError } = useSWRSubscription('/notice/event', (key, { next }) => {
    const eventSource = new EventSource(`${process.env.NEXT_PUBLIC_NOTICE}/api/eventsrc`);
    eventSource.onmessage = async (e: MessageEvent) => next(null, e.data);
    eventSource.onerror = async (error: Event) => next(error);
    return () => eventSource.close();
  });
  data && setNoticeList(data);
  sub && setNoticeList(data ? [...data, sub] : [sub]);
  return { noticeList, setNoticeList, error: error || eventSrcError };
}
