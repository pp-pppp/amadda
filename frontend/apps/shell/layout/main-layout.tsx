import { MAIN } from './layouts.css';
import type { ReactNode } from 'react';

export interface LayoutProps {
  children: ReactNode;
}

export function MainLayout({ children }: LayoutProps) {
  return <div className={MAIN}>{children}</div>;
}
