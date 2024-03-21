import type { ReactNode } from 'react';
import { BASE } from './schedule-edit-frame.css';

interface ScheduleEditFrameProps {
  children: ReactNode;
}
export function ScheduleEditFrame({ children }: ScheduleEditFrameProps) {
  return <div className={BASE}>{children}</div>;
}
