import React from 'react';
import type { ComponentPropsWithoutRef, ReactNode } from 'react';
import { BASE } from './label.css';

export interface LabelProps extends ComponentPropsWithoutRef<'label'> {
  htmlFor: string;
  children: ReactNode;
}

export function Label({ htmlFor, children }: LabelProps) {
  return (
    <label className={BASE} htmlFor={htmlFor}>
      {children}
    </label>
  );
}
