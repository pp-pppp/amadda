import { List } from '@amadda/external-temporal';
import { ReactNode } from 'react';

export function GroupBody({ children, ...props }: { children: ReactNode }) {
  return <List.Ul {...props}>{children}</List.Ul>;
}
