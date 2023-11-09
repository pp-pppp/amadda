import React from 'react';
import type { HTMLAttributes, FormEvent, ReactNode } from 'react';

export interface FormProps extends HTMLAttributes<HTMLFormElement> {
  formName: string;
  onSubmit: (e: FormEvent) => void;
  children: ReactNode;
}

export function Form({ formName, onSubmit, children }: FormProps) {
  return (
    <form
      name={formName}
      onSubmit={e => {
        e.preventDefault();
        return onSubmit(e);
      }}
    >
      {children}
    </form>
  );
}
