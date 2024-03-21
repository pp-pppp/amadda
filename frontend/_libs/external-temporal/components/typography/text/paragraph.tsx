import React from 'react';
import type { ComponentPropsWithoutRef, ReactNode } from 'react';

import { TYPE } from './paragraph.css';
import { textColors } from '#/style/text.css';

export interface PghProps extends ComponentPropsWithoutRef<'p'> {
  type?: 'text' | 'caption';
  color?: keyof typeof textColors;
  children: ReactNode;
}
export function P({ type = 'text', color = 'black', children }: PghProps) {
  const className = `${textColors[color]} ${TYPE[type]}`;

  return <p className={className}>{children}</p>;
}
