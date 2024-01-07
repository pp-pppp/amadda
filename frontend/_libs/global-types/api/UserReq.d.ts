export interface UserJwtRequest {
  kakaoId: string;
  imageUrl: string;
}
export interface UserInitRequest {
  kakaoId: string;
  imageUrl: string;
  userName: string;
  userId: string;
}
export interface UserIdCheckRequest {
  userId: string;
}
export interface UserNameCheckRequest {
  userName: string;
}
