import { HTMLAttributes } from 'react';
import { VARIANTS } from './H.css';

interface Props extends HTMLAttributes<HTMLHeadingElement> {}
export default function H1({ ...props }: Props) {
  const className = VARIANTS[1];
  return <h1 className={className} {...props}></h1>;
}
