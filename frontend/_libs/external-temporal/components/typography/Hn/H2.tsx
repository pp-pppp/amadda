import React from 'react';
import { COLOR, VARIANTS } from './H.css';
import { HnProps } from './HnProps';

export function H2({ color = 'black', children }: HnProps) {
  const className = `${VARIANTS[2]} ${COLOR[color]}`;
  return <h2 className={className}>{children}</h2>;
}
