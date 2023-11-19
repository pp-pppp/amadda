import React from 'react';
import { ReactNode } from 'react';
import { Icon, Span } from 'external-temporal';
import { BASE, SELECTED } from './MobileMonthlyPlate.css';

export interface MobileMonthPlateProps {
  dateType: keyof typeof dateColor;
  isScheduled?: boolean;
  isSelected?: boolean;
  onClick?(): void;
  children: ReactNode;
}

export interface dateColorValues {
  [key: string]: 'black' | 'warn' | 'lightgrey';
}

const dateColor: dateColorValues = {
  weekday: 'black',
  weekend: 'warn',
  other: 'lightgrey',
};

export function MobileMonthlyPlate({
  dateType,
  isScheduled = false,
  isSelected = false,
  onClick = () => {},
  children,
}: MobileMonthPlateProps) {
  const className = isSelected ? `${BASE} ${SELECTED}` : `${BASE}`;

  return (
    <div className={className} onClick={onClick}>
      <Span color={dateColor[dateType]}>{children}</Span>
      {isScheduled && <Icon type="dot" color="darkgrey" />}
    </div>
  );
}
