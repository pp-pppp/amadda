import React from 'react';
import type { HTMLAttributes } from 'react';
import { VARIANTS } from './H.css';
import { HnProps } from './H1';

export function H5({ children }: HnProps) {
  const className = VARIANTS[5];
  return <h5 className={className}>{children}</h5>;
}
