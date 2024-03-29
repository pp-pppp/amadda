import React from 'react';
import type { ComponentPropsWithoutRef } from 'react';
import { MENU } from './navigation.css';
import { Icon, IconProps } from '@amadda/external-temporal';

export interface MenuProps extends ComponentPropsWithoutRef<'button'> {
  data?: any;
  iconType: IconProps['type'];
  onClick: () => void;
}
export function Menu({ iconType, data, onClick }: MenuProps) {
  return (
    <button className={MENU} onClick={onClick}>
      <Icon type={iconType} color="key" cursor="pointer" size="28" />
    </button>
  );
}
