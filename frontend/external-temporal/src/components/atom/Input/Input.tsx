/* eslint-disable @typescript-eslint/no-unused-vars */
import React, { ChangeEvent, HTMLAttributes } from 'react';
import { TYPE } from './Input.css';

interface Props extends HTMLAttributes<HTMLInputElement> {
  type: 'text' | 'checkbox';
  validator?(target: string): boolean;
  id: string;
  name: string;
  onChange(e: React.ChangeEvent<HTMLInputElement>): void;
  disabled: boolean;
  placeholder?: string;
  value: string;
  checked?: boolean;
}

export default function Input({
  type,
  validator = target => true,
  id,
  name,
  onChange,
  disabled,
  placeholder = '',
  value,
  checked,
}: Props) {
  return (
    <input
      className={TYPE[type]}
      type={type}
      id={id}
      name={name}
      onChange={e => validator(e.target.value) && onChange(e)}
      disabled={disabled}
      placeholder={placeholder}
      value={value}
      checked={checked}
    ></input>
  );
}
