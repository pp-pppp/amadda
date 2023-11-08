import React from 'react';
import type { HTMLAttributes } from 'react';
import {
  CONTAINER_BASE,
  CONTAINER_VARIANT,
  SLIDER,
  SWITCH_BASE,
  SWITCH_VARIANT,
} from './Switch.css';
interface SwitchProps extends HTMLAttributes<HTMLInputElement> {
  id: string;
  selected: boolean;
  value: string;
}
export function Switch({ id, selected, value }: SwitchProps) {
  const isSelected = selected ? 'selected' : 'unselected';
  const CONTAINER = `${CONTAINER_BASE} ${CONTAINER_VARIANT[isSelected]}`;
  const SWITCH = `${SWITCH_BASE} ${SWITCH_VARIANT[isSelected]}`;
  return (
    <div className={CONTAINER}>
      <div className={SLIDER}>
        <input className={SWITCH} id={id} type="checkbox" value={value} />
      </div>
    </div>
  );
}
