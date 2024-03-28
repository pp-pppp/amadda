import type { Dispatch, SetStateAction, ChangeEvent, RefObject } from 'react';

export interface UseForm<T extends Object> {
  values: T;
  setValues?: Dispatch<SetStateAction<T>>;
  refValues?: Record<keyof T, any> | null;
  handleChange: (e: ChangeEvent<HTMLInputElement & HTMLTextAreaElement>) => Promise<void>;
  valid?: boolean;
  invalidFields?: Array<Record<keyof T, string>>;
  refs?: Record<keyof T, RefObject<HTMLInputElement>> | null;
  submit: (data: any) => any;
  isLoading?: boolean;
  response?: unknown;
}

export interface UseFormArgs<T extends Object> {
  initialValues: T;
  onSubmit: (data: T) => any;
  validator?: (data: T) => boolean;
  refInputNames?: (keyof T)[];
  setExternalStoreValues?: (formdata: T) => void;
  setExternalStoreData?: (data: UseForm<T>) => void;
}
