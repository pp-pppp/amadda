import axios from 'axios';
import useSWR from 'swr';
import { useDateStore } from '@SCH/store/dateStore';
import { useEffect } from 'react';
import { http } from '@SCH/utils/http';

const fetcher = url =>
  axios
    .get(`${process.env.NEXT_PUBLIC_SCHEDULE}/api/schedule/serverTime`)
    .then(res => res.data)
    .then(data => data.data);

export default function useServerTime() {
  const { year, month, date } = useDateStore();
  const { data } = useSWR('/api/schedule/serverTime', fetcher);
  useEffect(() => {
    if (data) {
      useDateStore.setState(state => ({
        ...state,
        year: data.split('-')[0],
        month: data.split('-')[1],
        date: data.split('-')[2],
      }));
    }
  }, [data]);

  return {};
}
