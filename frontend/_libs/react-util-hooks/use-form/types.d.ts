import { useForm } from './use-form';

export interface UseFormArgs<T> {
  initialValues: T;
  onSubmit: (data: T) => any;
  validator?: (data: T) => Array<Record<keyof T, string>>;
  refInputNames?: (keyof T)[];
  setExternalStoreValues?: (formdata: T) => void;
  setExternalStoreData?: (data: UseForm) => void;
}

export interface UseForm<T> {
  values: T;
  setValues: Dispatch<SetStateAction<T>>;
  refValues: Record<keyof T, any> | null;
  handleChange: (e: ChangeEvent<HTMLInputElement | HTMLTextAreaElement>) => Promise<void>;
  invalidFields: Array<Record<keyof T, string>>;
  refs: Record<(typeof refInputNames)[number], RefObject<HTMLInputElement>> | null;
  submit: (data: any) => any;
  isLoading: boolean;
  response: unknown;
}
