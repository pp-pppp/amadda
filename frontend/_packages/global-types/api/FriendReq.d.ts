export interface GroupCreateRequest {
  ownerSeq: number;
  groupName: string;
  userSeqs: Array<number>;
}
export interface GroupPutRequest {
  ownerSeq: number;
  groupName: string;
  userSeqs: Array<number>;
}
export interface FriendRequestRequest {
  userSeq: number; // 친구요청을 보내는 사람
  targetSeq: number; // 친구요청을 보낸 상대
}
