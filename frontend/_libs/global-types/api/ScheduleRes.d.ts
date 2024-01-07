import { CategoryListReadResponse } from './CategoryRes';
import { CommentReadResponse } from './CommentRes';
import { UserReadResponse, UserScheduleResponse } from './UserRes';

export interface ScheduleCreateResponse {
  scheduleSeq: number;
}
export interface ScheduleUpdateResponse {
  scheduleSeq: number;
}
export interface ScheduleDetailReadResponse {
  scheduleSeq: number;
  scheduleContent: string;
  isTimeSelected: boolean;
  isDateSelected: boolean;
  isAllday: boolean;
  scheduleStartAt: string;
  scheduleEndAt: string;
  isAuthorizedAll: boolean;
  authorizedUser: UserReadResponse;
  participants: Array<UserScheduleResponse>;
  comments: CommentReadResponse[];
  alarmTime: string;
  scheduleName: string;
  scheduleMemo: string;
  category: CategoryReadResponse;
}
export interface ScheduleListReadResponse {
  scheduleSeq: number;
  isDateSelected: boolean;
  isTimeSelected: boolean;
  isAllday: boolean;
  scheduleStartAt: string;
  scheduleEndAt: string;
  isAuthorizedAll: boolean;
  authorizedUser: UserReadResponse;
  participants: Array<UserReadResponse>;
  isFinised: boolean;
  alarmTime: string;
  scheduleName: string;
  category: CategoryReadResponse;
}
export interface ScheduleSearchResponse {
  [date?: string]: Array<ScheduleListReadResponse>;
  unscheduled?: Array<ScheduleListReadResponse>;
}
export interface ScheduleListUpdateResponse {
  scheduleSeq: number;
  isTimeSelected: boolean;
  isDateSelected: boolean;
  isAllday: boolean;
  isAuthorizedAll: boolean;
  scheduleStartAt: string;
  scheduleEndAt: string;
  loginUser: UserReadResponse;
  participants: Array<UserReadResponse>;
  alarmTime: string;
  scheduleName: string;
  category: CategoryListReadResponse;
  scheduleMemo: string;
}
export interface ServerTimeResponse {
  code: number;
  status: string;
  message: string;
  data: string;
}
