import React from 'react';
import type { HTMLAttributes, MouseEvent, ReactNode } from 'react';
import { SIZE, VARIANTS } from './BtnRound.css';

export interface BtnRoundProps extends HTMLAttributes<HTMLButtonElement> {
  type: 'button' | 'submit';
  variant: keyof typeof VARIANTS;
  size?: keyof typeof SIZE;
  disabled?: boolean;
  onClick?: (e: MouseEvent) => void;
  children: ReactNode;
}
export function BtnRound({
  type,
  variant,
  disabled,
  size = 'M',
  children,
  onClick,
}: BtnRoundProps) {
  const className = `${VARIANTS[variant]} ${SIZE[size]}`;
  return (
    <button
      className={className}
      disabled={disabled}
      type={type}
      onClick={onClick}
    >
      {children}
    </button>
  );
}
