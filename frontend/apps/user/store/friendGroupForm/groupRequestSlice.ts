import { GroupCreateRequest, GroupUpdateRequest, User } from '@amadda/global-types';
import { StateCreator } from 'zustand';

export type GroupRequest = {
  setGroupName: (data: string) => void;
  userSeqs: number[];
  users: User[];
  addUser: (user: User | User[]) => void;
  deleteUser: (user: User) => void;
} & GroupCreateRequest &
  GroupUpdateRequest;

export const groupRequestSlice: StateCreator<GroupRequest, [], [], GroupRequest> = set => ({
  groupName: '',
  userSeqs: [],
  users: [],
  setGroupName: data => set(state => ({ groupName: data })),
  addUser: user =>
    set(state => {
      if (Array.isArray(user)) {
        return { userSeqs: user.map(friend => friend.userSeq), users: user };
      } else {
        const userSeq = new Set([...state.userSeqs, user.userSeq]);
        const users = new Set([...state.users, user]);
        return { userSeqs: Array.from(userSeq), users: Array.from(users) };
      }
    }),
  deleteUser: user => set(state => ({ userSeqs: state.userSeqs.filter(userSeq => userSeq !== user.userSeq) })),
});
