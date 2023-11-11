import React from 'react';
import type { HTMLAttributes, ReactNode } from 'react';
import { COLOR } from './Texts.css';

export interface SpanProps extends HTMLAttributes<HTMLSpanElement> {
  color?: keyof typeof COLOR;
  children: ReactNode;
}
export function Span({ color = 'black', children }: SpanProps) {
  return <span className={COLOR[color]}>{children}</span>;
}
