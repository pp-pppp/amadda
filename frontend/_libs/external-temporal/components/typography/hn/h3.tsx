import React from 'react';
import { COLOR, VARIANTS } from './headings.css';
import { HnProps } from './heading-props';

export function H3({ color = 'black', children }: HnProps) {
  const className = `${VARIANTS[2]} ${COLOR[color]}`;
  return <h2 className={className}>{children}</h2>;
}
