import useSWR, { mutate } from 'swr';
import { clientFetch } from 'connection';
import { CategoryReadResponse } from 'amadda-global-types';

const getCategory = () => clientFetch.get<Array<CategoryReadResponse>>(`${process.env.NEXT_PUBLIC_SCHEDULE}/api/schedule/user/category`);

export function useGetCategory() {
  const { data, error, isLoading, mutate } = useSWR('/api/schedule/user/category', getCategory);
  return { category: data ? data : [], SWRerror: error, categoryIsLoading: isLoading, categoryMutate: mutate };
}

const addCategory = async (categoryName: string) => {
  await clientFetch.post(`${process.env.NEXT_PUBLIC_USER}/api/category`, categoryName);
  const result = await clientFetch.get<Array<CategoryReadResponse>>(`${process.env.NEXT_PUBLIC_SCHEDULE}/api/schedule/user/category`);
  return result;
};

export function usePostCategory(data: string) {
  mutate('/api/schedule/user/category', addCategory(data), { revalidate: true });
}
