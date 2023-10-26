import React from 'react';
import { HTMLAttributes } from 'react';
import { BASE } from './Label.css';

interface Props extends HTMLAttributes<HTMLLabelElement> {
  label: string;
  id: string;
}
export default function Label({ label, id }: Props) {
  return (
    <label className={BASE} htmlFor={id}>
      {label}
    </label>
  );
}
