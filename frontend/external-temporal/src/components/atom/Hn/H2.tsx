import { HTMLAttributes } from 'react';
import { VARIANTS } from './H.css';

interface Props extends HTMLAttributes<HTMLHeadingElement> {}
export default function H2({ ...props }: Props) {
  const className = VARIANTS[2];
  return <h2 className={className} {...props}></h2>;
}
