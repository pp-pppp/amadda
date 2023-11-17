export interface UserJwtRequest {
  kakaoId: string;
  imageUrl: string;
}
export interface UserInitRequest {
  userSeq: string;
  imageUrl: string;
  nickName: string;
  userId: string;
}
export interface UserIdCheckRequest {
  userId: string;
}
export interface UserNameCheckRequest {
  userName: string;
}
