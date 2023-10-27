import { HTMLAttributes } from 'react';
import { COLOR } from './Texts.css';

interface Props extends HTMLAttributes<HTMLParagraphElement> {
  color?: keyof typeof COLOR;
}
export default function P({ color = 'black', ...props }: Props) {
  return <p className={COLOR[color]} {...props}></p>;
}
