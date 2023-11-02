import type { HTMLAttributes } from 'react';
import { VARIANTS } from './H.css';
import { HnProps } from './H1';

export function H3({ children }: HnProps) {
  const className = VARIANTS[3];
  return <h3 className={className}>{children}</h3>;
}
