import { INDEXLAYOUT_MOBILE } from './layouts.css';
import type { ReactNode } from 'react';

export interface LayoutProps {
  children: ReactNode;
}

export function IndexLayout({ children }: LayoutProps) {
  return <div className={INDEXLAYOUT_MOBILE}>{children}</div>;
}
