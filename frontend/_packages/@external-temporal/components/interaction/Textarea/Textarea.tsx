import React from 'react';
import type { HTMLAttributes } from 'react';
import { SIZE } from './Textarea.css';

export interface TextAreaProps extends HTMLAttributes<HTMLTextAreaElement> {
  value: string;
  placeholder: string;
  height: keyof typeof SIZE;
}

export function Textarea({ value, placeholder, height }: TextAreaProps) {
  return (
    <textarea
      className={SIZE[height]}
      placeholder={placeholder}
      value={value}
      wrap="on"
    />
  );
}
