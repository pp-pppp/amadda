import { useEffect, useState } from 'react';

export interface CalendarDateType {
  date: number;
  monthType: 'prev' | 'curr' | 'next';
  index: number;
}

export const useCalendarDates = (
  year: string,
  month: string,
  date: string
): CalendarDateType[][] => {
  const [calendarDates, setCalendarDates] = useState<CalendarDateType[][]>([]);

  useEffect(() => {
    const generateCalendarDates = (): CalendarDateType[][] => {
      const dates: CalendarDateType[][] = [];

      // 날짜 계산
      // 1. month월 1일의 요일 구하기 : 0~6 일~토
      const firstForm = new Date(parseInt(year), parseInt(month) - 1, 1);
      const firstDay = firstForm.getDay() === 7 ? 0 : firstForm.getDay();

      // 2. 현재 월의 말일 구하기
      const lastForm = new Date(parseInt(year), parseInt(month), 0);
      const lastDate = lastForm.getDate();

      // 3. 전월의 말일 구하기
      const prevLastForm = new Date(parseInt(year), parseInt(month) - 1, 0);
      const prevLastDate = prevLastForm.getDate();

      let currDate = 1;
      let nextMonth = 1;
      let idx = 1;

      for (let row = 0; row < 6; row++) {
        const week: CalendarDateType[] = [];
        for (let col = 0; col < 7; col++) {
          if (row === 0 && col < firstDay) {
            week.push({
              date: prevLastDate - (firstDay - col) + 1,
              monthType: 'prev',
              index: idx,
            });
          } else if (currDate <= lastDate) {
            week.push({ date: currDate, monthType: 'curr', index: idx });
            currDate++;
          } else {
            week.push({ date: nextMonth, monthType: 'next', index: idx });
            nextMonth++;
          }

          idx++;
        }
        dates.push(week);
      }

      return dates;
    };

    setCalendarDates(generateCalendarDates());
  }, [year, month]);

  return calendarDates;
};
