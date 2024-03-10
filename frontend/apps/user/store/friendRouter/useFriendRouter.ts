import { create } from 'zustand';
import { pageSlice, Page } from './pageSlice';

export const useFriendRouter = create<Page>()((...a) => ({
  ...pageSlice(...a),
}));
