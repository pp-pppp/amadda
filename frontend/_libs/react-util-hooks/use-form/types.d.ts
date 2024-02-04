import { useForm } from './useForm';

export type UseFormArgs<T> = {
  initialValues: T;
  onSubmit: (data: T) => Promise<unknown>;
  validator?: (data: T) => Array<Record<keyof T, string>>;
  refInputNames?: (keyof T)[];
  setExternalStoreValues?: (formdata: T) => void;
  setExternalStoreData?: (data: UseForm) => void;
};

export type UseForm<T> = {
  values: T;
  setValues: Dispatch<SetStateAction<T>>;
  refValues: Record<keyof T, any> | null;
  handleChange: (e: ChangeEvent<HTMLInputElement | HTMLTextAreaElement>) => Promise<void>;
  invalidFields: Array<Record<keyof T, string>>;
  refs: Record<(typeof refInputNames)[number], RefObject<HTMLInputElement>> | null;
  submit: (data: any) => Promise<unknown>;
  isLoading: boolean;
  response: unknown;
};
