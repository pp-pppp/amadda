import { HTMLAttributes } from 'react';
import { COLOR } from './Texts.css';

interface Props extends HTMLAttributes<HTMLSpanElement> {
  color?: keyof typeof COLOR;
}
export default function Span({ color = 'black', ...props }: Props) {
  return <span className={COLOR[color]} {...props}></span>;
}
