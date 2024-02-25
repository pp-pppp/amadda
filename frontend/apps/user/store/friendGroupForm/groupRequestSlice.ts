import { GroupCreateRequest, GroupUpdateRequest, User } from '@amadda/global-types';
import { StateCreator } from 'zustand';

export type GroupRequest = {
  setGroupName: (data: string) => void;
  addUser: (user: User) => void;
  deleteUser: (user: User) => void;
} & GroupCreateRequest &
  GroupUpdateRequest;

export const groupRequestSlice: StateCreator<GroupRequest, [], [], GroupRequest> = set => ({
  groupName: '',
  userSeqs: [],
  setGroupName: data => set(state => ({ groupName: data })),
  addUser: user =>
    set(state => {
      const Users = new Set([...state.userSeqs, user.userSeq]);
      return { userSeqs: Array.from(Users) };
    }),
  deleteUser: user => set(state => ({ userSeqs: state.userSeqs.filter(userSeq => userSeq !== user.userSeq) })),
});
