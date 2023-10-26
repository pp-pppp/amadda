import { HTMLAttributes } from 'react';
import { TYPE } from './Caption.css';

interface Props extends HTMLAttributes<HTMLParagraphElement> {
  type: 'none' | 'desc' | 'warn';
}

export default function Caption({ type, ...props }: Props) {
  return <p className={TYPE[type]} {...props}></p>;
}
