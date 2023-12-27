import { clientFetch } from 'connection';
import { AlarmReadResponse } from 'amadda-global-types';
import { useEffect, useState } from 'react';
import useSWR from 'swr';
const fetcher = () => clientFetch.get<Array<AlarmReadResponse>>(`${process.env.NEXT_PUBLIC_NOTICE}/api/alarm`);

export function useGetNotice() {
  const [noticeList, setNoticeList] = useState<Array<AlarmReadResponse>>([]);
  const { data, error } = useSWR<Array<AlarmReadResponse>>('/api/alarm', fetcher);
  data && setNoticeList(data);
  return { noticeList, setNoticeList, SWRerror: error };
}
