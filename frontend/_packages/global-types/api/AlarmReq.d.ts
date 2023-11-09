export interface AlarmConfigRequest {
  userSeq: number;
  alarmType:
    | 'FRIEND_REQUEST'
    | 'FRIEND_ACCEPT'
    | 'SCHEDULE_ASSIGNED'
    | 'MENTIONED'
    | 'SCHEDULE_UPDATE'
    | 'SCHEDULE_NOTIFICATION';
}
