import React, { useEffect, useState } from 'react';
import { GRID } from './mobile-calendar-date.css';
import { MobileMonthlyPlate } from '@/components/mobile-monthly-plate/mobile-monthly-plate';
import { useCalendarDates } from '@/hooks/schedule/use-calendar-info';
import { useDateStore } from '@/store/schedule/date-store';
import { useMonthlySchedule } from '@/hooks/schedule/use-schedule-list';
import { useCategoryStore } from '@/store/schedule/category-store';
import useServerTime from '@/hooks/schedule/use-server-time';

export interface DateInfo {
  year: string;
  month: string;
  date: string;
}

export function MobileCalendarDate() {
  // 전역 store에서 연월 정보 가져오기
  useServerTime();
  const { selectedYear, selectedMonth, selectedDate } = useDateStore();
  const { selectedCategorySeq, selectedAll, selectedNone } = useCategoryStore();
  const { monthlyScheduleList, setMonthlyScheduleList, SWRerror: error } = useMonthlySchedule();
  const [chosen, setChosen] = useState('');
  const calendarDates = useCalendarDates(selectedYear, selectedMonth, selectedDate);

  useEffect(() => setChosen(selectedDate), [selectedDate]);

  const categoryCheck = (dateString: string) => {
    const isContain = monthlyScheduleList.dateString.some(schedule => selectedCategorySeq.includes(schedule.category.categorySeq));

    return isContain;
  };

  // 일정 유무 확인
  const isScheduled = (date: number): boolean => {
    if (typeof monthlyScheduleList === 'undefined') {
      return false;
    }

    const dateString = selectedYear + '-' + selectedMonth + '-' + date.toString().padStart(2, '0');

    return dateString in monthlyScheduleList && categoryCheck(dateString) ? true : false;
  };

  const isSelected = (monthType: 'prev' | 'curr' | 'next', date: number): boolean => {
    if (monthType !== 'curr') return false;
    return parseInt(selectedDate) === date ? true : false;
  };

  const dateTypeMapper = (monthType: 'prev' | 'curr' | 'next', colIndex: number) => {
    if (monthType === 'prev' || monthType === 'next') return 'OTHER';
    else if (colIndex % 7 === 0 || colIndex % 7 === 6) return 'WEEKEND';
    return 'WEEKDAY';
  };

  const goPrev = (currDate: number) => {
    const newMonth = parseInt(selectedMonth) - 1 > 0 ? parseInt(selectedMonth) - 1 : 12;
    const newYear = newMonth === 12 ? parseInt(selectedYear) - 1 : selectedYear;
    const newDate = currDate.toString().padStart(2, '0');

    useDateStore.setState(() => ({
      selectedYear: newYear.toString(),
      selectedMonth: newMonth.toString(),
      selectedDate: newDate,
    }));
    setChosen(newDate);
  };

  const goNext = (currDate: number) => {
    const newMonth = parseInt(selectedMonth) + 1 < 13 ? parseInt(selectedMonth) + 1 : 1;
    const newYear = newMonth === 1 ? parseInt(selectedYear) + 1 : selectedYear;
    const newDate = currDate.toString().padStart(2, '0');

    useDateStore.setState(() => ({
      selectedYear: newYear.toString(),
      selectedMonth: newMonth.toString(),
      selectedDate: newDate,
    }));
    setChosen(newDate);
  };

  return (
    <div className={GRID}>
      {calendarDates.map((week, rowIndex) => {
        return week.map(({ date, monthType }, colIndex) => (
          <MobileMonthlyPlate
            key={`${rowIndex}-${colIndex}`}
            dateType={dateTypeMapper(monthType, colIndex)}
            isScheduled={monthType === 'curr' && isScheduled(date)}
            isSelected={isSelected(monthType, date)}
            onClick={() => {
              if (monthType === 'curr') {
                useDateStore.setState(state => ({
                  ...state,
                  selectedDate: date.toString().padStart(2, '0'),
                }));
              } else if (monthType === 'prev') {
                goPrev(date);
              } else if (monthType === 'next') {
                goNext(date);
              }
            }}
          >
            {date}
          </MobileMonthlyPlate>
        ));
      })}
    </div>
  );
}
