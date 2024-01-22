import { ScheduleEditFormProps } from '@SCH/components/ScheduleEdit/formdata';
import { initFormValues } from '@SCH/constants/SCHEDULE_EDIT_INIT';
import { formToRequest } from '@SCH/utils/convertFormData';
import { ScheduleCreateRequest, ScheduleUpdateRequest } from '@amadda/global-types';
import { useForm } from '@amadda/react-util-hooks';
import { StateCreator, create } from 'zustand';

export interface InputSlice {
  requestData: ScheduleCreateRequest | ScheduleUpdateRequest | null;
  setRequestData: (by: { values: ScheduleEditFormProps; refValues: ScheduleEditFormProps }) => void;
}

export interface UseFormSlice extends ReturnType<typeof useForm<ScheduleEditFormProps>> {
  setUseFormData: (by: { data: ReturnType<typeof useForm<ScheduleEditFormProps>> }) => void;
  setResult: (by: { data: ReturnType<typeof useForm<ScheduleEditFormProps>> }) => void;
}

const createInputSlice: StateCreator<InputSlice & UseFormSlice, [], [], InputSlice> = set => ({
  requestData: null,
  setRequestData: () => set(state => ({ requestData: formToRequest({ ...state.values, ...state.refValues }) })),
});

const createUseFormSlice: StateCreator<InputSlice & UseFormSlice, [], [], UseFormSlice> = set => ({
  key: '',
  values: initFormValues,
  setValues: v => v,
  refValues: null,
  handleChange: e => new Promise(() => {}),
  invalids: [],
  refs: null,
  submit: e => new Promise(() => {}),
  isLoading: false,
  result: null,
  setUseFormData: by =>
    set(_ => ({
      key: by.data.key,
      values: by.data.values,
      setValues: by.data.setValues,
      refValues: by.data.refValues,
      handleChange: by.data.handleChange,
      invalids: by.data.invalids,
      refs: by.data.refs,
      submit: by.data.submit,
      isLoading: by.data.isLoading,
    })),
  setResult: by =>
    set(_ => ({
      result: by.data.result,
    })),
});

export const useScheduleEditStore = create<InputSlice & UseFormSlice>()((...args) => ({
  ...createInputSlice(...args),
  ...createUseFormSlice(...args),
}));
