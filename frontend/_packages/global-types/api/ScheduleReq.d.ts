import { UserScheduleResponse } from './UserRes';

export interface ScheduleCreateRequest {
  scheduleContent: string;
  isTimeSelected: boolean;
  isDateSelected: boolean;
  isAllday: boolean;
  isAuthorizedAll: boolean;
  scheduleStartAt: string;
  scheduleEndAt: string;
  participants: Array<UserReadResponse>;
  alarmTime: string;
  scheduleName: string;
  scheduleMemo: string;
  categorySeq: number;
}

export interface ScheduleUpdateRequest {
  scheduleContent: string;
  isTimeSelected: boolean;
  isDateSelected: boolean;
  isAllday: boolean;
  isAuthorizedAll: boolean;
  scheduleStartAt: string;
  scheduleEndAt: string;
  participants: Array<UserReadResponse>;
  alarmTime: string;
  scheduleName: string;
  scheduleMemo: string;
  categorySeq: number;
}
