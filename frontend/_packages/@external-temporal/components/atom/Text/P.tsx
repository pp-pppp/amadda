import React from 'react';
import type { HTMLAttributes } from 'react';
import { COLOR } from './Texts.css';

export interface PghProps extends HTMLAttributes<HTMLParagraphElement> {
  color?: keyof typeof COLOR;
  children: string;
}
export function P({ color = 'black', children }: PghProps) {
  return <p className={COLOR[color]}>{children}</p>;
}
