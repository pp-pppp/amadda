import React, { useEffect } from 'react';
import { MobileCalendarIndex } from './MobileCalendarIndex/MobileCalendarIndex';
import { MobileCalendarDate } from './MobileCalendarDate/MobileCalendarDate';
import { Spacing } from '@amadda/external-temporal';
import useServerTime from '@/hooks/schedule/useServerTime';
import { useDateStore } from '@/store/schedule/dateStore';

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
