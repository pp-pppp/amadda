import { ScheduleCreateRequest } from '@amadda/global-types';
import { ScheduleEditFormData } from '../components/ScheduleEdit/formdata';
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
  key: '',
  values: initFormValues,
  setValues: () => {},
  refValues: null,
  result: undefined,
  handleChange: e => new Promise(() => {}),
  invalids: [],
  refs: null,
  submit: data => new Promise(() => {}),
  isLoading: false,
};
