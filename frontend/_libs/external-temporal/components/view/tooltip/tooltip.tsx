import React from 'react';
import { BASE } from './tooltip.css';
import { P } from '#/components/typography/text/paragraph';

export interface TooltipProps {
  children: string;
}
export function Tooltip({ children }: TooltipProps) {
  return (
    <div className={BASE}>
      <P>{children}</P>
    </div>
  );
}
