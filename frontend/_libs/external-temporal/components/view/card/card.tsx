import React from 'react';
import type { ComponentPropsWithoutRef, ReactNode } from 'react';
import { BASE, BORDER } from './card.css';

export interface CardProps extends ComponentPropsWithoutRef<'div'> {
  children: ReactNode;
  border?: boolean;
}

export function Card({ children, border = true }: CardProps) {
  const className = `${BASE} ${border && BORDER}`;
  return <div className={className}>{children}</div>;
}
