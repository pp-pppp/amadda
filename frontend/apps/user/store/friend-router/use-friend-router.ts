import { create } from 'zustand';
import { pageSlice, Page } from './page-slice';

export const useFriendRouter = create<Page>()((...a) => ({
  ...pageSlice(...a),
}));
