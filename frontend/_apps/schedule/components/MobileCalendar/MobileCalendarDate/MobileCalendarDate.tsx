import React, { useEffect, useState } from 'react';
import { GRID } from './MobileCalendarDate.css';
import { MobileMonthlyPlate } from '@SCH/components/MobileMonthlyPlate/MobileMonthlyPlate';
import { useCalendarDates } from '@SCH/hooks/useCalendarInfo';
import { useDateStore } from '@SCH/store/dateStore';
import { useScheduleListStore } from '@SCH/store/scheduleListStore';

export interface DateInfo {
  year: string;
  month: string;
  date: string;
}

export function MobileCalendarDate() {
  // 전역 store에서 연월 정보 가져오기
  const { year, month, date } = useDateStore();
  const { schedules } = useScheduleListStore();
  const [selectedDate, setSelectedDate] = useState('');
  const calendarDates = useCalendarDates(year, month, date);

  useEffect(() => setSelectedDate(date), [date]);

  // 일정 유무 확인
  const isScheduled = (date: number): boolean => {
    const dateString =
      year + '-' + month + '-' + date.toString().padStart(2, '0');
    return dateString in schedules ? true : false;
  };

  const isSelected = (
    monthType: 'prev' | 'curr' | 'next',
    date: number
  ): boolean => {
    if (monthType !== 'curr') return false;
    return parseInt(selectedDate) === date ? true : false;
  };

  const dateTypeMapper = (
    monthType: 'prev' | 'curr' | 'next',
    colIndex: number
  ) => {
    if (monthType === 'prev' || monthType === 'next') return 'other';
    else if (colIndex % 7 === 0 || colIndex % 7 === 6) return 'weekend';
    return 'weekday';
  };

  const goPrev = (currDate: number) => {
    const newMonth = parseInt(month) - 1 > 0 ? parseInt(month) - 1 : 12;
    const newYear = newMonth === 12 ? parseInt(year) - 1 : year;
    const newDate = currDate.toString().padStart(2, '0');

    useDateStore.setState(() => ({
      year: newYear.toString(),
      month: newMonth.toString(),
      date: newDate,
    }));
    setSelectedDate(newDate);
  };

  const goNext = (currDate: number) => {
    const newMonth = parseInt(month) + 1 < 13 ? parseInt(month) + 1 : 1;
    const newYear = newMonth === 1 ? parseInt(year) + 1 : year;
    const newDate = currDate.toString().padStart(2, '0');

    useDateStore.setState(() => ({
      year: newYear.toString(),
      month: newMonth.toString(),
      date: newDate,
    }));
    setSelectedDate(newDate);
  };

  return (
    <div className={GRID}>
      {calendarDates.map((week, rowIndex) => (
        <React.Fragment key={rowIndex}>
          {week.map(({ date, monthType }, colIndex) => (
            <MobileMonthlyPlate
              key={`${rowIndex}-${colIndex}`}
              dateType={dateTypeMapper(monthType, colIndex)}
              isScheduled={monthType === 'curr' && isScheduled(date)}
              isSelected={isSelected(monthType, date)}
              onClick={() => {
                if (monthType === 'curr') {
                  useDateStore.setState(state => ({
                    ...state,
                    date: date.toString().padStart(2, '0'),
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
          ))}
        </React.Fragment>
      ))}
    </div>
  );
}
