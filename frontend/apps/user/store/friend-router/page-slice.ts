import type { StateCreator } from 'zustand';

export const ROUTES = ['READ', 'SEARCH', 'EDIT', 'ADD'] as const;

export interface Page {
  PATH: (typeof ROUTES)[number];
  updating: number | null;
  setUpdating: (to: number | null) => void;
  PushToFriend: (to: (typeof ROUTES)[number]) => void;
}
export const pageSlice: StateCreator<Page, [], [], Page> = set => ({
  PATH: 'READ',
  updating: null,
  setUpdating: to => set(state => ({ updating: to })),
  PushToFriend: to => set(state => ({ PATH: to })),
});
