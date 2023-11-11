import React from 'react';
import { ReactNode } from 'react';
import { Icon, Span } from 'external-temporal';
import { BASE, SELECTED } from './MobileMonthlyPlate.css';

export interface MobileMonthPlateProps {
  dateType: keyof typeof dateColor;
  isScheduled?: boolean;
  isSelected?: boolean;
  children: string;
}

export interface dateColorValues {
  [key: string]: 'black' | 'warn';
}

const dateColor: dateColorValues = {
  weekday: 'black',
  weekend: 'warn',
};

export function MobileMonthlyPlate({
  dateType,
  isScheduled = false,
  isSelected = false,
  children,
}: MobileMonthPlateProps) {
  const className = isSelected ? `${BASE} ${SELECTED}` : `${BASE}`;

  return (
    <div className={className}>
      <Span color={dateColor[dateType]}>{children}</Span>
      {isScheduled && <Icon type="dot" color="darkgrey" />}
    </div>
  );
}
