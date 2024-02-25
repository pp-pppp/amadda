import { FriendSearchInput, friendSearchInputSlice } from './friendSearchInputSlice';
import { create } from 'zustand';

export const useFriendSearchStore = create<FriendSearchInput>()((...a) => ({
  ...friendSearchInputSlice(...a),
}));
