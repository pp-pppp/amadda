import React, { useEffect } from 'react';
import { MobileCalendarIndex } from './mobile-calendar-index/mobile-calendar-index';
import { MobileCalendarDate } from './mobile-calendar-date/mobile-calendar-date';
import { Spacing } from '@amadda/external-temporal';
import useServerTime from '@/hooks/schedule/use-server-time';
import { useDateStore } from '@/store/schedule/date-store';

export function MobileCalendar() {
  useServerTime();
  const { selectedYear, selectedMonth, selectedDate } = useDateStore();

  return (
    <>
      {/* <MobileCalendarIndex /> */}
      <Spacing dir="v" size="1" />
      <MobileCalendarDate />
    </>
  );
}
