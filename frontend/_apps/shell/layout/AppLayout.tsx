import { APPLAYOUT_MOBILE } from './Layout.css';

export interface LayoutProps {
  children: React.ReactNode;
}

export function AppLayout({ children }: LayoutProps) {
  return <div className={APPLAYOUT_MOBILE}>{children}</div>;
}
