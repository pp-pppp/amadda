import { HTMLAttributes } from 'react';
import { BASE } from './Caption.css';
import P from '../Text/P';
import { COLOR } from '../Text/Texts.css';

interface Props extends HTMLAttributes<HTMLParagraphElement> {
  color?: keyof typeof COLOR;
}

export default function Caption({ color = 'lightgrey', ...props }: Props) {
  const className = `${BASE} ${COLOR[color]}`;

  return <P className={className} {...props}></P>;
}
