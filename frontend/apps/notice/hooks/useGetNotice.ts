import { clientFetch } from '@amadda/fetch';
import { AlarmReadResponse } from '@amadda/global-types';
import useSWR from 'swr';

const fetcher = () => clientFetch.get<Array<AlarmReadResponse>>(`${process.env.NEXT_PUBLIC_NOTICE}/api/alarm`);

export function useGetNotice() {
  const { data, error, isLoading } = useSWR<Array<AlarmReadResponse>>('/api/alarm', fetcher);
  //TODO: 데이터 출처가 eventsource, api 두 곳일 때 SWR를 어떻게 사용할 수 있을지
  return { noticeList: data, error, isLoading };
}
