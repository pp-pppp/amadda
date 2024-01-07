import React from 'react';
import { COLOR, VARIANTS } from './H.css';
import { HnProps } from './HnProps';

export function H6({ color = 'black', children }: HnProps) {
  const className = `${VARIANTS[6]} ${COLOR[color]}`;
  return <h6 className={className}>{children}</h6>;
}
