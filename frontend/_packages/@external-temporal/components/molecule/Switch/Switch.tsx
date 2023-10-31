import React from 'react';
import { HTMLAttributes } from 'react';
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
}
export function Switch({ id, selected }: SwitchProps) {
  return (
    <div
      className={`${CONTAINER_BASE} ${
        CONTAINER_VARIANT[selected ? 'selected' : 'unselected']
      }`}
    >
      <div className={SLIDER}>
        <input
          className={`${SWITCH_BASE} ${
            SWITCH_VARIANT[selected ? 'selected' : 'unselected']
          }`}
          id={id}
          type="checkbox"
          value={''}
        />
      </div>
    </div>
  );
}
