import React from 'react';
import type { HTMLAttributes } from 'react';
import { BASE } from './Label.css';

export interface LabelProps extends HTMLAttributes<HTMLLabelElement> {
  htmlFor: string;
  children: string;
}

export function Label({ htmlFor, children }: LabelProps) {
  return (
    <label className={BASE} htmlFor={htmlFor}>
      {children}
    </label>
  );
}
