import React from 'react';
import type { HTMLAttributes, ReactNode } from 'react';
import { VARIANTS } from './H.css';

export interface HnProps extends HTMLAttributes<HTMLHeadingElement> {
  children: ReactNode;
}

export function H1({ children }: HnProps) {
  const className = VARIANTS[1];
  return <h1 className={className}>{children}</h1>;
}
