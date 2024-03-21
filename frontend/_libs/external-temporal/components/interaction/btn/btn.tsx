import React from 'react';
import type { ComponentPropsWithoutRef, MouseEvent, ReactNode } from 'react';

import { VARIANTS } from './btn.css';

export interface BtnProps extends ComponentPropsWithoutRef<'button'> {
  type: 'button' | 'submit';
  variant: keyof typeof VARIANTS;
  disabled?: boolean;
  onClick?: (e: MouseEvent) => void;
  children: ReactNode;
}

export function Btn({ type, variant, disabled, onClick, children }: BtnProps) {
  const className = `${VARIANTS[variant]}`;
  return (
    <button className={className} disabled={disabled} type={type} onClick={onClick}>
      {children}
    </button>
  );
}
