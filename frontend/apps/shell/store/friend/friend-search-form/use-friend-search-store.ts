import { FriendSearchInput, friendSearchInputSlice } from './friend-search-form-slice';
import { create } from 'zustand';

export const useFriendSearchStore = create<FriendSearchInput>()((...a) => ({
  ...friendSearchInputSlice(...a),
}));
