export interface User {
  userSeq: number;
  userName: string;
  userId: string;
  imageUrl: string;
}
export interface Group {
  groupSeq: number;
  groupName: string;
  groupMember: Array<User>;
}

export type FriendReadResponse = Array<Group>;
