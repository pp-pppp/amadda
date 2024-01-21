import { create } from 'zustand';
import { useForm } from './useForm';
import { useFormArgs } from './types';

export const useFormStore = <T>({ ...args }: useFormArgs<T>) => {
  const data = useForm<T>({ ...args });
  return create<ReturnType<typeof useForm<T>>>(_ => ({ ...data }));
};
