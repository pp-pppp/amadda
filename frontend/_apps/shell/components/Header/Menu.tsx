import React from 'react';
import type { HTMLAttributes } from 'react';
import { MENU } from './Header.css';
import { Icon, IconProps } from 'external-temporal';

export interface MenuProps extends HTMLAttributes<HTMLButtonElement> {
  data?: any;
  iconType: IconProps['type'];
  onClick: () => void;
}
export function Menu({ iconType, data, onClick }: MenuProps) {
  return (
    <button className={MENU} onClick={onClick}>
      <Icon type={iconType} color="key" cursor="pointer" />
    </button>
  );
}
