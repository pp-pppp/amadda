import React from 'react';
import { ReactNode } from 'react';
import { Icon, Span } from 'external-temporal';
import { BASE, DATE, SELECTED, SELECTED_SPAN, SPAN } from './MobileMonthlyPlate.css';

export interface MobileMonthPlateProps {
  dateType: keyof typeof SPAN;
  isScheduled?: boolean;
  isSelected?: boolean;
  onClick?(): void;
  children: ReactNode;
}

// export interface dateColorValues {
//   [key: string]: 'black' | 'warn' | 'lightgrey';
// }

// const dateColor: dateColorValues = {
//   weekday: 'black',
//   weekend: 'warn',
//   other: 'lightgrey',
// };

export function MobileMonthlyPlate({ dateType, isScheduled = false, isSelected = false, onClick = () => {}, children }: MobileMonthPlateProps) {
  const className = isSelected ? `${DATE} ${SELECTED}` : `${DATE}`;
  const spanStyle = isSelected ? SELECTED_SPAN : SPAN[dateType];

  return (
    <div className={BASE} onClick={onClick}>
      <div className={className}>
        <span className={spanStyle}>{children}</span>
      </div>
      {isScheduled && <Icon type="dot" color="darkgrey" />}
    </div>
  );
}
