import React from 'react';
import { COLOR, VARIANTS } from './H.css';
import { HnProps } from './HnProps';

export function H5({ color = 'black', children }: HnProps) {
  const className = `${VARIANTS[5]} ${COLOR[color]}`;
  return <h5 className={className}>{children}</h5>;
}
