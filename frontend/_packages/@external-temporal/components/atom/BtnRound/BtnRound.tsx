import React from 'react';
import type { HTMLAttributes } from 'react';
import { SIZE, VARIANTS } from './BtnRound.css';

export interface BtnRoundProps extends HTMLAttributes<HTMLButtonElement> {
  type: 'button' | 'submit';
  variant: keyof typeof VARIANTS;
  size?: keyof typeof SIZE;
  disabled: boolean;
  onClick: () => void;
  children: string;
}
export function BtnRound({
  type,
  variant,
  disabled,
  size = 'M',
  children,
}: BtnRoundProps) {
  const className = `${VARIANTS[variant]} ${SIZE[size]}`;
  return (
    <button
      className={className}
      disabled={disabled}
      type={type}>
      {children}
    </button>
  );
}
