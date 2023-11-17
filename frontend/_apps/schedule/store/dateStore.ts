import { create } from 'zustand';

export interface DateState {
  year: string;
  month: string;
  date: string;
}

export const useDateStore = create<DateState>()(set => ({
  year: '',
  month: '',
  date: '',
}));
