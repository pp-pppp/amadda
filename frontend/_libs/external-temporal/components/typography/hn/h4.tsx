import React from 'react';
import { COLOR, VARIANTS } from './headings.css';
import { HnProps } from './heading-props';

export function H4({ color = 'black', children }: HnProps) {
  const className = `${VARIANTS[4]} ${COLOR[color]}`;
  return <h4 className={className}>{children}</h4>;
}
