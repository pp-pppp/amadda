import { create } from 'zustand';

export interface DateState {
  selectedYear: string;
  selectedMonth: string;
  selectedDate: string;
}

export const useDateStore = create<DateState>()(set => ({
  selectedYear: '',
  selectedMonth: '',
  selectedDate: '',
}));
