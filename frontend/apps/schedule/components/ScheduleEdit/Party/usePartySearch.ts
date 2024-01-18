import { clientFetch } from '@amadda/fetch';
import useSWR from 'swr';

export const usePartySearch = async (key: string) => {
  const { data, error, isLoading } = useSWR(`${process.env.NEXT_PUBLIC_USER}/api/friend?searchKey=${key}`, clientFetch.get);
  return data;
};
