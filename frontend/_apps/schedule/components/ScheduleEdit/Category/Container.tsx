import React from 'react';
import type { ReactNode } from 'react';
import { CONTAINER } from './Category.css';
export interface ContainerProps {
  children: ReactNode;
}
export function Container({ children }) {
  return <div className={CONTAINER}>{children}</div>;
}
