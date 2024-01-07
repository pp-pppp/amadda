import React from 'react';
import { COLOR, VARIANTS } from './H.css';
import { HnProps } from './HnProps';

export function H1({ color = 'black', children }: HnProps) {
  const className = `${VARIANTS[6]} ${COLOR[color]}`;
  return <h1 className={className}>{children}</h1>;
}
