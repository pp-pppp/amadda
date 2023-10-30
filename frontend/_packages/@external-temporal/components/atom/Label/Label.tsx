import type { HTMLAttributes } from 'react';
import React from 'react';
import { BASE } from './Label.css';

export interface LabelProps extends HTMLAttributes<HTMLLabelElement> {
  id: string;
  children: string;
}

export function Label({ id, children }: LabelProps) {
  return (
    <label
      className={BASE}
      htmlFor={id}>
      {children}
    </label>
  );
}
