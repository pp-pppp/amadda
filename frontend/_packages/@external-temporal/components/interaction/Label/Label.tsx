import React from 'react';
import type { HTMLAttributes, ReactNode } from 'react';
import { BASE } from './Label.css';

export interface LabelProps extends HTMLAttributes<HTMLLabelElement> {
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
