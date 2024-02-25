import { create } from 'zustand';
import { GroupRequest, groupRequestSlice } from './groupRequestSlice';

export const useGroupRequestStore = create<GroupRequest>()((...a) => ({
  ...groupRequestSlice(...a),
}));
