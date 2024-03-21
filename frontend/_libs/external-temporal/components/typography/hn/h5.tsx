import React from 'react';
import { COLOR, VARIANTS } from './headings.css';
import { HnProps } from './heading-props';

export function H5({ color = 'black', children }: HnProps) {
  const className = `${VARIANTS[5]} ${COLOR[color]}`;
  return <h5 className={className}>{children}</h5>;
}
