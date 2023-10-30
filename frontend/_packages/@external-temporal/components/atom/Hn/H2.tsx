import React from 'react';
import type { HTMLAttributes } from 'react';
import { VARIANTS } from './H.css';

export interface Props extends HTMLAttributes<HTMLHeadingElement> {
  children: string;
}

export function H2({ children }: Props) {
  const className = VARIANTS[2];
  return <h2 className={className}>{children}</h2>;
}
