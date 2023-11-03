import type { HTMLAttributes } from 'react';
import { VARIANTS } from './H.css';
import { HnProps } from './H1';

export function H6({ children }: HnProps) {
  const className = VARIANTS[6];
  return <h6 className={className}>{children}</h6>;
}
