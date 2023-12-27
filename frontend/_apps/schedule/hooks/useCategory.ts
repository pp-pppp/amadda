import useSWR from 'swr';
import { useEffect, useState } from 'react';
import { clientFetch } from 'connection';
import { CategoryReadResponse } from 'amadda-global-types';

const fetcher = () => clientFetch.get<Array<CategoryReadResponse>>(`${process.env.NEXT_PUBLIC_SCHEDULE}/api/schedule/user/category`);

export default function useCategory() {
  const [categories, setCategories] = useState<Array<CategoryReadResponse>>([]);
  const { data, error, isLoading } = useSWR('/api/schedule/user/category', fetcher);

  useEffect(() => {
    data && setCategories(data);
  }, [data]);

  return { categories, setCategories, SWRerror: error };
}
