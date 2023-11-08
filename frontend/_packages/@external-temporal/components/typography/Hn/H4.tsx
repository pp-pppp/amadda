import React from 'react';
import type { HTMLAttributes } from 'react';
import { VARIANTS } from './H.css';
import { HnProps } from './H1';

export function H4({ children }: HnProps) {
  const className = VARIANTS[4];
  return <h4 className={className}>{children}</h4>;
}
