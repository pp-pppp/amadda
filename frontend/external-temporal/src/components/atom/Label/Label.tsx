import React from 'react';
import { HTMLAttributes } from 'react';
import { BASE } from './Label.css';

interface Props extends HTMLAttributes<HTMLLabelElement> {
  id: string;
}

export default function Label({ id, ...props }: Props) {
  return <label className={BASE} htmlFor={id} {...props}></label>;
}
