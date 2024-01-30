import { CategoryReadResponse, FriendReadResponse, UserReadResponse } from '@amadda/global-types';

export interface ScheduleEditFormProps {
  startYear: string;
  startMonth: string;
  startDate: string;
  startTime: string;
  startMinute: string;
  endYear: string;
  endMonth: string;
  endDate: string;
  endTime: string;
  endMinute: string;
  category: Array<CategoryReadResponse>;
  scheduleName: string;
  participants: Array<UserReadResponse>;
  scheduleStartAt: string;
  scheduleEndAt: string;
  isDateSelected: boolean;
  isTimeSelected: boolean;
  isAllday: boolean;
  isAuthorizedAll: boolean;
  scheduleContent: string;
  alarmTime: keyof typeof CREATE.ALARMS | string;
  categorySeq?: string;
  scheduleMemo: string;
  partySearchInput: string;
  partySearchResult: FriendReadResponse;
  categoryInput: string;
}
