import React from 'react';
import type { FocusEvent, HTMLAttributes, KeyboardEvent } from 'react';

import { ChangeEvent } from 'react';
import { TYPE } from './Input.css';

export interface InputProps extends HTMLAttributes<HTMLInputElement> {
  type: 'text' | 'checkbox' | 'number';
  validator?: (target: string | number) => boolean;
  id: string;
  name: string;
  onChange: (e: ChangeEvent<HTMLInputElement>) => void;
  onKeyDown?: (e: KeyboardEvent) => void;
  onFocus?: (e: FocusEvent) => void;
  disabled?: boolean;
  placeholder?: string;
  value?: string | number;
  checked?: boolean;
  autoComplete?: undefined | 'off';
}

export function Input({
  type,
  validator = target => true,
  id,
  name,
  onChange,
  onKeyDown = e => {},
  onFocus = e => {},
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
      onKeyDown={e => onKeyDown(e)}
      onFocus={e => onFocus(e)}
      placeholder={placeholder}
      type={type}
      value={value}
      autoComplete={autoComplete}
      style={style}
    />
  );
}
