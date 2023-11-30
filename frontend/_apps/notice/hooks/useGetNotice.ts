import { http } from '@N/utils/http';
import { AlarmReadResponse } from 'amadda-global-types';
import { useEffect, useState } from 'react';
import useSWR from 'swr';
const fetcher = () => http.get<Array<AlarmReadResponse>>(`${process.env.NEXT_PUBLIC_NOTICE}/api/alarm`).then(res => res.data);

export function useGetNotice() {
  const [noticeList, setNoticeList] = useState<Array<AlarmReadResponse>>([]);
  const { data, error } = useSWR<Array<AlarmReadResponse>>('/api/alarm', fetcher);
  data && setNoticeList(data);
  return { noticeList, setNoticeList, SWRerror: error };
}
