import React from 'react';
import type { HTMLAttributes } from 'react';
import { VARIANTS } from './H.css';

export interface HnProps extends HTMLAttributes<HTMLHeadingElement> {
  children: string;
}

export function H1({ children }: HnProps) {
  const className = VARIANTS[1];
  return <h1 className={className}>{children}</h1>;
}
