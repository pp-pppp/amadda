import { ScheduleCreateRequest, ScheduleDetailReadResponse } from '@amadda/global-types';
import { NextRouter } from 'next/router';
import useSWR from 'swr';
import { clientFetch } from '@amadda/fetch';
import { ScheduleEditFormProps } from '../formdata';

export function useScheduleEdit(router: NextRouter) {
  if (router.basePath.includes('edit')) {
    const { scheduleSeq } = router.query;
    const { data, error, isLoading } = useSWR(`${process.env.NEXT_PUBLIC_SCHEDULE}/api/schedule/${scheduleSeq}`, clientFetch.get<ScheduleDetailReadResponse>);
    return data;
  }
}

export const initFormValues: ScheduleEditFormProps = {
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
