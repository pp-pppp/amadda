import { ReactNode } from 'react';
import { Icon, Span } from '#/index';
import { BASE, SELECTED } from './MobileMontlyPlate.css';

export interface MobileMonthPlateProps {
  dateType: keyof typeof dateColor;
  isSelected?: boolean;
  children: string;
  // children: ReactNode;
}

export interface dateColorValues {
  [key: string]: 'black' | 'warn';
}

const dateColor: dateColorValues = {
  weekday: 'black',
  weekend: 'warn',
};

export default function MobileMonthPlate({
  dateType,
  isSelected = false,
  children,
}: MobileMonthPlateProps) {
  const rand = Math.floor(Math.random() * 31) + 1;
  isSelected = children === rand.toString() ? true : false;
  const className = isSelected ? `${BASE}` : `${BASE} ${SELECTED}`;

  return (
    <div className={className}>
      <Span color={dateColor[dateType]}>{children}</Span>
      <Icon type="dot" color="darkgrey" />
    </div>
  );
}
