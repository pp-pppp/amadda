import useSWR from 'swr';
import { useDateStore } from '@/store/schedule/dateStore';
import { useEffect } from 'react';
import { clientFetch } from '@amadda/fetch';

const fetcher = () => clientFetch.get<string>(`${process.env.NEXT_PUBLIC_SCHEDULE}/api/schedule/server-time`);

export default function useServerTime() {
  const { selectedYear, selectedMonth, selectedDate } = useDateStore();
  const { data } = useSWR('/api/schedule/server-time', fetcher);

  useEffect(() => {
    if (data) {
      useDateStore.setState(state => ({
        ...state,
        selectedYear: data.split('-')[0],
        selectedMonth: data.split('-')[1],
        selectedDate: data.split('-')[2],
      }));
    }
  }, [data]);

  if (data) return { data };
  else return { data: '' };
}
