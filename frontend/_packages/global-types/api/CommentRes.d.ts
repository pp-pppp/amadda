import { UserReadResponse } from './UserRes';

export interface CommentReadResponse {
  commentSeq: number;
  user: UserReadResponse;
  commentContent: string;
  createdAt: string;
}
