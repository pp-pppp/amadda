import React, { useEffect } from 'react';
import { MobileCalendarIndex } from './MobileCalendarIndex/MobileCalendarIndex';
import { MobileCalendarDate } from './MobileCalendarDate/MobileCalendarDate';
import { Spacing } from 'external-temporal';
import useServerTime from '@SCH/hooks/useServerTime';
import { useDateStore } from '@SCH/store/dateStore';

export function MobileCalendar() {
  useServerTime();
  const { year, month, date } = useDateStore();

  return (
    <>
      {/* <MobileCalendarIndex /> */}
      <Spacing dir="v" size="1"></Spacing>
      <MobileCalendarDate />
    </>
  );
}
