import { ScheduleEditFormData } from '@SCH/components/ScheduleEdit/ScheduleEditForm/formdata';
import { initFormValues } from '@SCH/constants/SCHEDULE_EDIT_INIT';
import { formToRequest } from '@SCH/utils/convertFormData';
import { ScheduleCreateRequest, ScheduleUpdateRequest } from '@amadda/global-types';
import type { UseForm } from '@amadda/react-util-hooks';
import { StateCreator, create } from 'zustand';

export interface InputSlice {
  requestData: ScheduleCreateRequest | ScheduleUpdateRequest | null;
  setRequestData: () => void;
}

export interface UseFormSlice extends UseForm<ScheduleEditFormData> {
  setUseFormValues: (by: ScheduleEditFormData) => void;
  setUseFormData: (by: UseForm<ScheduleEditFormData>) => void;
  setResult: (by: UseForm<ScheduleEditFormData>) => void;
}

const createInputSlice: StateCreator<InputSlice & UseFormSlice, [], [], InputSlice> = set => ({
  requestData: null,
  setRequestData: () =>
    set(state => ({
      requestData: formToRequest({ ...state.values, ...state.refValues }),
    })),
});

const createUseFormSlice: StateCreator<InputSlice & UseFormSlice, [], [], UseFormSlice> = set => ({
  values: initFormValues,
  setValues: (v: ScheduleEditFormData) => {},
  refValues: null,
  handleChange: e => new Promise(() => {}),
  invalids: [],
  refs: null,
  submit: e => new Promise(() => {}),
  isLoading: false,
  result: null,
  setUseFormValues: by => {
    console.log(by, 'useformvalues');
    return set(state => ({
      values: by,
    }));
  },
  setUseFormData: by =>
    set(state => ({
      values: by.values,
      setValues: by.setValues,
      refValues: by.refValues,
      handleChange: by.handleChange,
      invalids: by.invalids,
      refs: by.refs,
      submit: by.submit,
      isLoading: by.isLoading,
    })),
  setResult: by =>
    set(state => ({
      result: by.result,
    })),
});

export const useScheduleEditStore = create<InputSlice & UseFormSlice>()((...args) => ({
  ...createInputSlice(...args),
  ...createUseFormSlice(...args),
}));
