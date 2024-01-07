import { APPLAYOUT_MOBILE } from './Layout.css';
import type { ReactNode } from 'react';

export interface LayoutProps {
  children: ReactNode;
}

export function AppLayout({ children }: LayoutProps) {
  return <div className={APPLAYOUT_MOBILE}>{children}</div>;
}
