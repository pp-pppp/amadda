import { CategoryListReadResponse } from './CategoryRes';
import { CommentReadResponse } from './CommentRes';
import { UserReadResponse, UserScheduleResponse } from './UserRes';

export interface ScheduleCreateResponse {
  scheduleSeq: number;
}
export interface ScheduleDetailReadResponse {
  scheduleSeq: number;
  scheduleContent: string;
  isTimeSelected: boolean;
  isDateSelected: boolean;
  isAllday: boolean;
  isAuthorizedAll: boolean;
  scheduleStartAt: string;
  scheduleEndAt: string;
  participants: Array<UserScheduleResponse>;
  comments: CommentReadResponse;
  alarmTime: string;
  scheduleName: string;
  scheduleMemo: string;
  categoryName: string;
}
export interface ScheduleListReadResponse {
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
  category: {
    // 카테고리 response dto
  };
  scheduleMemo: string;
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
