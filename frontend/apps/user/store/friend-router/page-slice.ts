import type { StateCreator } from 'zustand';

export const ROUTES = ['READ', 'SEARCH', 'UPDATE', 'ADD'] as const;

export interface Page {
  PATH: (typeof ROUTES)[number];
  updatingGroupSeq: number | null;
  setUpdatingGroupSeq: (to: number | null) => void;
  PushToFriend: (to: (typeof ROUTES)[number]) => void;
}
export const pageSlice: StateCreator<Page, [], [], Page> = set => ({
  PATH: 'READ',
  updatingGroupSeq: null,
  setUpdatingGroupSeq: to => set(state => ({ updatingGroupSeq: to })),
  PushToFriend: to => set(state => ({ PATH: to })),
});
