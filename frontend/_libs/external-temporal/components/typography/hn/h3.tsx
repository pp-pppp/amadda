import React from 'react';
import { COLOR, VARIANTS } from './headings.css';
import { HnProps } from './heading-props';

export function H3({ color = 'black', children }: HnProps) {
  const className = `${VARIANTS[3]} ${COLOR[color]}`;
  return <h3 className={className}>{children}</h3>;
}
