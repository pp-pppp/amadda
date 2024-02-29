import type { StateCreator } from 'zustand';

export const ROUTES = ['READ', 'SEARCH', 'EDIT', 'ADD'] as const;

export interface Page {
  PATH: (typeof ROUTES)[number];
  editing: number | null;
  setEditing: (to: number | null) => void;
  PushToFriend: (to: (typeof ROUTES)[number]) => void;
}
export const pageSlice: StateCreator<Page, [], [], Page> = set => ({
  PATH: 'READ',
  editing: null,
  setEditing: to => set(state => ({ editing: to })),
  PushToFriend: to => set(state => ({ PATH: to })),
});
