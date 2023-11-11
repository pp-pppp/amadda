import { Flex, Span } from '#/index';
import React from 'react';
import { BASE } from './MobileCalendarIndex.css';

export function MobileCalendarIndex() {
  return (
    <div className={BASE}>
      <Span color="warn">일</Span>
      <Span>월</Span>
      <Span>화</Span>
      <Span>수</Span>
      <Span>목</Span>
      <Span>금</Span>
      <Span color="warn">토</Span>
    </div>
  );
}
