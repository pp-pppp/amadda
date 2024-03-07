import React from 'react';
import { COLOR, VARIANTS } from './headings.css';
import { HnProps } from './heading-props';

export function H1({ color = 'black', children }: HnProps) {
  const className = `${VARIANTS[6]} ${COLOR[color]}`;
  return <h1 className={className}>{children}</h1>;
}
