export interface UserScheduleResponse {
  userSeq: number;
  userName: string;
  userId: string;
  imageUrl: string;
  isFriend: boolean;
}
export interface UserReadResponse {
  userSeq: number;
  userName: string;
  userId: string;
  imageUrl: string;
}
export interface UserJwtResponse {
  accessToken: string;
  refreshToken: string;
  refreshAccessKey: string;
  isInited: boolean;
  kakaoId: string;
}
export interface UserJwtInitResponse {
  accessToken: string;
  refreshToken: string;
  refreshAccessKey: string;
  isInited: boolean;
}
export interface UserAccessResponse {
  isExpired: boolean;
  isBroken: boolean;
  refreshAccessKey: string;
}
export interface UserIdCheckResponse {
  isDuplicated: boolean;
  isValid: boolean;
}
export interface UserRelationResponse {
  userSeq: number;
  userName: string;
  userId: string;
  imageUrl: string;
  isFriend: boolean;
}
export interface UserNameCheckResponse {
  isValid: boolean;
}
