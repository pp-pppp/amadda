import React from 'react';
import type { HTMLAttributes, ReactNode } from 'react';
import { BASE, BORDER } from './Card.css';

export interface CardProps extends HTMLAttributes<HTMLDivElement> {
  children: ReactNode;
  border?: boolean;
}

export function Card({ children, border = true }: CardProps) {
  const className = `${BASE} ${border && BORDER}`;
  return <div className={className}>{children}</div>;
}
