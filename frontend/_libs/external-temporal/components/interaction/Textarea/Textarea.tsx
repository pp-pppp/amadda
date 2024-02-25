import React from 'react';
import type { ChangeEvent, FormEvent, ComponentPropsWithoutRef } from 'react';
import { SIZE } from './Textarea.css';

export interface TextAreaProps extends ComponentPropsWithoutRef<'textarea'> {
  value: string;
  id: string;
  name: string;
  placeholder: string;
  height: keyof typeof SIZE;
  onChange: (event: ChangeEvent<HTMLTextAreaElement>) => void;
}

export function Textarea({ id, value, name, placeholder, height, onChange }: TextAreaProps) {
  return <textarea id={id} name={name} className={SIZE[height]} placeholder={placeholder} value={value} wrap="on" onChange={onChange} />;
}
