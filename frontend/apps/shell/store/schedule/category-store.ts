import { create } from 'zustand';

export interface categoryState {
  selectedCategorySeq: number[];
  selectedAll: boolean;
  selectedNone: boolean;
}

export const useCategoryStore = create<categoryState>()(set => ({
  selectedCategorySeq: [],
  selectedAll: true,
  selectedNone: true,
}));
