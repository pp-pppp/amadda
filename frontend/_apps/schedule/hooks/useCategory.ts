import axios from 'axios';
import useSWR from 'swr';
import { useDateStore } from '@SCH/store/dateStore';
import { useEffect, useState } from 'react';
import { http } from '@SCH/utils/http';
import { CategoryReadResponse } from 'amadda-global-types';

const fetcher = url =>
  http
    .get<Array<CategoryReadResponse>>(
      `${process.env.NEXT_PUBLIC_USER}/api/user/category`
    )
    .then(res => res.data);

export default function useCategory() {
  const [categories, setCategories] = useState<Array<CategoryReadResponse>>([]);
  const { data, error, isLoading } = useSWR('/api/user/category', fetcher);

  useEffect(() => {
    data && setCategories(data);
  }, [data]);

  return { categories, setCategories, SWRerror: error };
}
