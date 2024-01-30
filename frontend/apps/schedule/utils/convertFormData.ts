import type { ScheduleEditFormData } from '@SCH/components/ScheduleEdit/formdata';
import type { ScheduleCreateRequest, ScheduleDetailReadResponse, ScheduleUpdateRequest } from '@amadda/global-types';
import { numberToString, stringToNumber } from './formatDate';

export const formToRequest = (data: ScheduleEditFormData): ScheduleCreateRequest | ScheduleUpdateRequest => {
  const startAt = numberToString(Number(data.startYear), Number(data.startMonth), Number(data.startDate), Number(data.startTime), Number(data.startMinute));
  const endAt = numberToString(Number(data.endYear), Number(data.endMonth), Number(data.endDate), Number(data.endTime), Number(data.endMinute));

  if (!data.categorySeq) {
    return {
      scheduleContent: data.scheduleContent,
      isTimeSelected: data.isTimeSelected,
      isDateSelected: data.isDateSelected,
      isAllday: data.isAllday,
      isAuthorizedAll: data.isAuthorizedAll,
      scheduleStartAt: startAt,
      scheduleEndAt: endAt,
      participants: data.participants,
      alarmTime: String(data.alarmTime),
      scheduleName: data.scheduleName,
      scheduleMemo: data.scheduleMemo,
    };
  } else
    return {
      scheduleContent: data.scheduleContent,
      isTimeSelected: data.isTimeSelected,
      isDateSelected: data.isDateSelected,
      isAllday: data.isAllday,
      isAuthorizedAll: data.isAuthorizedAll,
      scheduleStartAt: startAt,
      scheduleEndAt: endAt,
      participants: data.participants,
      alarmTime: String(data.alarmTime),
      scheduleName: data.scheduleName,
      scheduleMemo: data.scheduleMemo,
      categorySeq: Number(data.categorySeq),
    };
};

export const responseToForm = (data: ScheduleDetailReadResponse): ScheduleEditFormData => {
  const startAt = stringToNumber(data.scheduleStartAt).map(String);
  const endAt = stringToNumber(data.scheduleStartAt).map(String);

  const result: ScheduleEditFormData = {
    startYear: startAt[0],
    startMonth: startAt[1],
    startDate: startAt[2],
    startTime: startAt[3],
    startMinute: startAt[4],
    endYear: endAt[0],
    endMonth: endAt[1],
    endDate: endAt[2],
    endTime: endAt[3],
    endMinute: endAt[4],
    category: data.category,
    scheduleName: data.scheduleName,
    participants: data.participants,
    scheduleStartAt: data.scheduleStartAt,
    scheduleEndAt: data.scheduleEndAt,
    isDateSelected: data.isDateSelected,
    isTimeSelected: data.isTimeSelected,
    isAllday: data.isAllday,
    isAuthorizedAll: data.isAuthorizedAll,
    scheduleContent: data.scheduleContent,
    alarmTime: data.alarmTime,
    scheduleMemo: data.scheduleMemo,
    partySearchInput: '',
    partySearchResult: [],
    categoryInput: '',
  };
  return result;
};
