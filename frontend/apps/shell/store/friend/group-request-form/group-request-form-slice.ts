import { GroupCreateRequest, GroupUpdateRequest, User } from '@amadda/global-types';
import { UseForm } from '@amadda/react-util-hooks';
import { StateCreator } from 'zustand';

export interface GroupRequestForm {
  groupName: string;
  groupMembers: User[];
}

export type GroupRequestFormData = UseForm<GroupRequestForm> & {
  setFormData: (data: UseForm<GroupRequestForm>) => void;
  setGroup: (data: UseForm<GroupRequestForm>['values']) => void;
  userSeqs: number[];
  addUser: (user: User | User[]) => void;
  deleteUser: (user: User) => void;
};
export const groupRequestFormDataSlice: StateCreator<GroupRequestFormData, [], [], GroupRequestFormData> = set => ({
  setFormData: data => set(state => ({ ...data })),
  setGroup: data => set(state => ({ values: data })),
  values: {
    groupName: '',
    groupMembers: [],
  },
  setValues: () => {},
  handleChange: e => new Promise(() => {}),
  isLoading: false,
  submit: () => {},
  response: null,
  refs: null,
  valid: true,
  userSeqs: [],
  addUser: user =>
    set(state => {
      if (Array.isArray(user)) {
        return { userSeqs: user.map(friend => friend.userSeq), groupMembers: user };
      } else {
        const users = new Set([...state.values.groupMembers, user]);
        const userSeq = new Set([...state.userSeqs, user.userSeq]);
        return { values: { ...state.values, groupMembers: Array.from(users) }, userSeqs: Array.from(userSeq) };
      }
    }),
  deleteUser: user => set(state => ({ userSeqs: state.userSeqs.filter(userSeq => userSeq !== user.userSeq) })),
});
