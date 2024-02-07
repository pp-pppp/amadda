import type { ReactNode } from 'react';
import { BASE } from './ScheduleEditFrame.css';

interface ScheduleEditFrameProps {
  children: ReactNode;
}
export function ScheduleEditFrame({ children }: ScheduleEditFrameProps) {
  return <div className={BASE}>{children}</div>;
}
