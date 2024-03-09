import { ScheduleCreateRequest } from '@amadda/global-types';
import { ScheduleEditFormData } from '@/components/schedule-edit/schedule-edit-form/formdata';
import { useForm } from '@amadda/react-util-hooks';

export const initFormValues: ScheduleEditFormData = {
  startYear: '',
  startMonth: '',
  startDate: '',
  startTime: '',
  startMinute: '',
  endYear: '',
  endMonth: '',
  endDate: '',
  endTime: '',
  endMinute: '',
  category: [],
  scheduleName: '',
  participants: [],
  scheduleStartAt: '',
  scheduleEndAt: '',
  isDateSelected: false,
  isTimeSelected: false,
  isAllday: false,
  isAuthorizedAll: false,
  scheduleContent: '',
  alarmTime: '',
  categorySeq: '',
  scheduleMemo: '',
  partySearchInput: '',
  partySearchResult: [],
  categoryInput: '',
};

export const scheduleCreateRequestInit: ScheduleCreateRequest = {
  scheduleName: '',
  participants: [],
  scheduleStartAt: '',
  scheduleEndAt: '',
  isDateSelected: false,
  isTimeSelected: false,
  isAllday: false,
  isAuthorizedAll: false,
  scheduleContent: '',
  alarmTime: 'NONE',
  scheduleMemo: '',
};

export const refInputNames: Array<keyof ScheduleEditFormData> = [
  'startYear',
  'startMonth',
  'startDate',
  'startTime',
  'startMinute',
  'endYear',
  'endMonth',
  'endDate',
  'endTime',
  'endMinute',
  'category',
  'participants',
  'scheduleContent',
  'alarmTime',
  'categorySeq',
  'scheduleMemo',
  'partySearchResult',
];

export const USEFORM_RETURN_INIT: ReturnType<typeof useForm<ScheduleEditFormData>> = {
  values: initFormValues,
  setValues: () => {},
  refValues: null,
  response: undefined,
  handleChange: e => new Promise(() => {}),
  invalidFields: [],
  refs: null,
  submit: data => new Promise(() => {}),
  isLoading: false,
};
