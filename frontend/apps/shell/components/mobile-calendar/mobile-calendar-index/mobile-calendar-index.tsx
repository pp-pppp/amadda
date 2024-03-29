import React from 'react';
import { BASE } from './mobile-calendar-index.css';
import WEEKDAY from '@/constants/schedule/weekday';
import { Span } from '@amadda/external-temporal';

export function MobileCalendarIndex() {
  return (
    <div className={BASE}>
      <Span color="warn">{WEEKDAY.SUN}</Span>
      <Span>{WEEKDAY.MON}</Span>
      <Span>{WEEKDAY.TUE}</Span>
      <Span>{WEEKDAY.WED}</Span>
      <Span>{WEEKDAY.THU}</Span>
      <Span>{WEEKDAY.FRI}</Span>
      <Span color="warn">{WEEKDAY.SAT}</Span>
    </div>
  );
}
