export interface GroupCreateRequest {
  groupName: string;
  userSeqs: Array<number>;
}
export interface GroupUpdateRequest {
  groupName: string;
  userSeqs: Array<number>;
}
export interface FriendRequestRequest {
  targetSeq: number;
}
