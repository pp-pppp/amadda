import { INDEXLAYOUT_MOBILE } from './Layout.css';

export interface LayoutProps {
  children: React.ReactNode;
}

export function IndexLayout({ children }: LayoutProps) {
  return <div className={INDEXLAYOUT_MOBILE}>{children}</div>;
}
