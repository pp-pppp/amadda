export interface UserJwtRequest {
  userSeq: string;
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
