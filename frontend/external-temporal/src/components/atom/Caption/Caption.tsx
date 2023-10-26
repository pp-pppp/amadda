import { HTMLAttributes } from 'react';
import { TYPE } from './Caption.css';

interface Props extends HTMLAttributes<HTMLParagraphElement> {
  type: 'none' | 'desc' | 'warn';
  caption?: string;
}

export default function Caption({ type, caption = '' }: Props) {
  return <p className={TYPE[type]}>{caption}</p>;
}
