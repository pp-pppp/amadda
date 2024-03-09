import React from 'react';
import { forwardRef } from 'react';
import { TYPE } from './input.css';
import { InputBaseProps } from './input-props';

export const RefInput = forwardRef<HTMLInputElement, InputBaseProps>(
  (
    {
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
      ...props
    }: InputBaseProps,
    ref
  ) => {
    return (
      <input
        ref={ref}
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
        {...props}
      />
    );
  }
);
