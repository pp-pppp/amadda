import type { HTMLAttributes } from 'react';
import React from 'react';
import { TYPE } from './Caption.css';

export interface CaptionProps extends HTMLAttributes<HTMLParagraphElement> {
  type: 'none' | 'desc' | 'warn';
  children?: string;
}

export function Caption({ type, children }: CaptionProps) {
  return <p className={TYPE[type]}>{children}</p>;
}
