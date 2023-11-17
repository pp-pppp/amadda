export interface AlarmReadResponse {
  alarmSeq?: number;
  content: string;
  isRead: boolean;
  alarmType:
    | 'FRIEND_REQUEST'
    | 'FRIEND_ACCEPT'
    | 'SCHEDULE_ASSIGNED'
    | 'MENTIONED'
    | 'SCHEDULE_UPDATE'
    | 'SCHEDULE_NOTI';
  isEnabled: boolean;
}
