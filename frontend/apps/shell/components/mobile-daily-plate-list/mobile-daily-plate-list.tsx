import { ReactNode } from 'react';
import { BASE } from './mobile-daily-plate-list.css';

export interface MobileDailyPlateListProps {
  children: ReactNode;
}
export function MobileDailyPlateList({ children }: MobileDailyPlateListProps) {
  return <div className={BASE}>{children}</div>;
}
