import { create } from 'zustand';
import { pageSlice, Page } from './pageSlice';

export const useFriendRouter = create<Page>()((...a) => ({
  ...pageSlice(...a),
}));
//고민 1. 라우터 zustand 로 구현하는게 맞음?
//사실 이 고민은 user 섹션이 module federation으로 빠지면서 나온 고민이 맞음...
//react context 라고 보는게 또 맞는것 같기도 해서..
