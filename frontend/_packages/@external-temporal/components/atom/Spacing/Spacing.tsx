import React, { HTMLAttributes } from 'react';
import { memo } from 'react';
import { HSIZE, VSIZE } from './Spacing.css';

export interface SpacingProps extends HTMLAttributes<HTMLDivElement> {
  dir?: 'h' | 'v';
  size?: '0.5' | '1' | '2' | '3' | '5' | '10';
}

export function Spacing({ dir = 'v', size = '1' }: SpacingProps) {
  const className = `${dir === 'v' ? VSIZE[size] : HSIZE[size]}`;
  return <div className={className} />;
}
export default Spacing;
