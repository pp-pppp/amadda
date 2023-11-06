import React from 'react';
import type { HTMLAttributes } from 'react';
import { COLOR } from './Texts.css';
import { TYPE } from './P.css';

export interface PghProps extends HTMLAttributes<HTMLParagraphElement> {
  type?: 'text' | 'caption';
  color?: keyof typeof COLOR;
  children: string;
}
export function P({ type = 'text', color = 'black', children }: PghProps) {
  const className = `${COLOR[color]} ${TYPE[type]}`;

  return <p className={className}>{children}</p>;
}
