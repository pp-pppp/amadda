import React from 'react';
import type { ComponentPropsWithoutRef, ReactNode } from 'react';

import { TYPE } from './P.css';
import { textColors } from '#/constants/Text.css';

export interface PghProps extends ComponentPropsWithoutRef<'p'> {
  type?: 'text' | 'caption';
  color?: keyof typeof textColors;
  children: ReactNode;
}
export function P({ type = 'text', color = 'black', children }: PghProps) {
  const className = `${textColors[color]} ${TYPE[type]}`;

  return <p className={className}>{children}</p>;
}
