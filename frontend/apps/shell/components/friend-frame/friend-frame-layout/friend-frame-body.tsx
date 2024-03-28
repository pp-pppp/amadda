import React from 'react';
import type { ReactNode } from 'react';

export interface FriendFrameProps {
  children: ReactNode;
}
export function FriendFrameBody({ children }) {
  return <div>{children}</div>;
}
