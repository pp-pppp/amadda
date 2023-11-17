import { ScheduleListReadResponse } from 'amadda-global-types';
import { create } from 'zustand';

// 연결 후 사용 예정
// export const useScheduleListStore = create<ScheduleListReadResponse[]>()(
//   set => []
// );

export interface TempType {
  schedules: {
    [date: string]: string[];
  };
}

export const useScheduleListStore = create<TempType>(set => ({
  schedules: {},
}));
