import React from 'react';
import { BASE } from './f-a-b.css';
import { Icon } from '#/components/view/icon/icon';

export function FAB() {
  return (
    <button className={BASE} type="button">
      <Icon color="white" cursor="pointer" type="scheduleadd" />
    </button>
  );
}
