import React from 'react';
import type { HTMLAttributes } from 'react';

import { ChangeEvent } from 'react';
import { TYPE } from './Input.css';

export interface InputProps extends HTMLAttributes<HTMLInputElement> {
  type: 'text' | 'checkbox';
  validator?: (target: string) => boolean;
  id: string;
  name: string;
  onChange: (e: React.ChangeEvent<HTMLInputElement>) => void;
  disabled: boolean;
  placeholder?: string;
  value: string;
  checked?: boolean;
  autoComplete?: undefined | 'off';
}

export function Input({
  type,
  validator = target => true,
  id,
  name,
  onChange,
  disabled,
  placeholder = '',
  value,
  checked,
  autoComplete = undefined,
  style = undefined,
}: InputProps) {
  return (
    <input
      checked={checked}
      className={TYPE[type]}
      disabled={disabled}
      id={id}
      name={name}
      onChange={e => validator(e.target.value) && onChange(e)}
      placeholder={placeholder}
      type={type}
      value={value}
      autoComplete={autoComplete}
      style={style}
    />
  );
}
