import React from 'react';
import type { HTMLAttributes } from 'react';
import { COLOR } from './Texts.css';

export interface SpanProps extends HTMLAttributes<HTMLSpanElement> {
  color?: keyof typeof COLOR;
  children: string;
}
export function Span({ color = 'black', children }: SpanProps) {
  return <span className={COLOR[color]}>{children}</span>;
}
