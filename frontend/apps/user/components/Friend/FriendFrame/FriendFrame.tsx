import React from 'react';
import type { ReactNode } from 'react';
import { FRAME } from './FriendFrame.css';

export interface FriendFrameProps {
  children: ReactNode;
}
export function FriendFrame({ children }) {
  return <div className={FRAME}>{children}</div>;
}
