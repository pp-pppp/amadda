import { HTMLAttributes } from 'react';
export interface FormProps extends HTMLAttributes<HTMLFormElement> {
  formName: string;
  onSubmit: (e: React.FormEvent) => void;
  children: React.ReactNode;
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
