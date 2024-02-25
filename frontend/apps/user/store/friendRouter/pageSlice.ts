import type { StateCreator } from 'zustand';

export const ROUTES = ['READ', 'SEARCH', 'EDIT', 'ADD'] as const;

export interface Page {
  PATH: (typeof ROUTES)[number];
  PushToFriend: (to: (typeof ROUTES)[number]) => void;
}
export const pageSlice: StateCreator<Page, [], [], Page> = set => ({
  PATH: 'READ',
  PushToFriend: to => set(state => ({ PATH: to })),
});
