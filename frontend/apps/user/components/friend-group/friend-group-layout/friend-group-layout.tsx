import React from 'react';
import type { ReactNode } from 'react';

export interface FriendFrameProps {}
export function FriendFrameLayout({ children }: { children: ReactNode }) {
  return <div>{children}</div>;
}
