import axios from 'axios';
import useSWR from 'swr';
import { useDateStore } from '@SCH/store/dateStore';
import { useEffect } from 'react';
import { http } from '@SCH/utils/http';

const fetcher = url =>
  axios
    .get(`${process.env.NEXT_PUBLIC_SCHEDULE}/api/schedule/server-time`)
    .then(res => res.data)
    .then(data => data.data);

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

  return {};
}
