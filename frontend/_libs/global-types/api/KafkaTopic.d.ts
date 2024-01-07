export interface AlarmFriendAccept {
  friendUserSeq: number;
  friendUserName: string;
}
export interface AlarmFriendRequest {
  friendRequestSeq: number;
  requestedUserSeq: number;
  requestedUserName: string;
}
export interface AlarmMentioned {
  scheduleSeq: number;
  scheduleName: string;
  writerUserSeq: number;
  writerUserName: string;
}
export interface AlarmScheduleAssigned {
  scheduleSeq: number;
  scheduleName: string;
  scheduleOwnerUserSeq: number;
  scheduleOwnerUserName: string;
}
export interface AlarmScheduleNotification {
  scheduleSeq: number;
  scheduleName: string;
  alarmTime: string;
  alarmDateTime: string;
}
export interface AlarmScheduleUpdate {
  scheduleSeq: number;
  scheduleName: string;
}
export interface GlobalSettingValue {
  isOn: boolean;
}
