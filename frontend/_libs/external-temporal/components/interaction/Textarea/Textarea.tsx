import React from 'react';
import type { ChangeEvent, FormEvent, ComponentPropsWithoutRef } from 'react';
import { SIZE } from './Textarea.css';

export interface TextAreaProps extends ComponentPropsWithoutRef<'textarea'> {
  value: string;
  placeholder: string;
  height: keyof typeof SIZE;
  onChange: (event: ChangeEvent<HTMLTextAreaElement>) => void;
}

export function Textarea({ value, placeholder, height, onChange }: TextAreaProps) {
  return <textarea className={SIZE[height]} placeholder={placeholder} value={value} wrap="on" onChange={onChange} />;
}
