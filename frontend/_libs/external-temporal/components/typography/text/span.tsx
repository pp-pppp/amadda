import React from 'react';
import type { ComponentPropsWithoutRef, ReactNode } from 'react';
import { textColors } from '#/style/text.css';

export interface SpanProps extends ComponentPropsWithoutRef<'span'> {
  color?: keyof typeof textColors;
  children: ReactNode;
}
export function Span({ color = 'black', children }: SpanProps) {
  const className = `${textColors[color]}`;
  return <span className={className}>{children}</span>;
}
