import React from 'react';
import type { HTMLAttributes, MouseEvent } from 'react';

import { VARIANTS } from './Btn.css';

export interface BtnProps extends HTMLAttributes<HTMLButtonElement> {
  type: 'button' | 'submit';
  variant: keyof typeof VARIANTS;
  disabled: boolean;
  onClick?: (e: MouseEvent) => void;
  children: string;
}

export function Btn({ type, variant, disabled, onClick, children }: BtnProps) {
  const className = `${VARIANTS[variant]}`;
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
