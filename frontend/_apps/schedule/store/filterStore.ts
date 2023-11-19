import { create } from 'zustand';

export interface filterState {
  isOpen: boolean;
}

export const useFilterStore = create<filterState>()(set => ({
  isOpen: false,
}));
