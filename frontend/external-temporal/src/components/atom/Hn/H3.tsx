import { HTMLAttributes } from 'react';
import { VARIANTS } from './H.css';

interface Props extends HTMLAttributes<HTMLHeadingElement> {}
export default function H3({ ...props }: Props) {
  const className = VARIANTS[3];
  return <h3 className={className} {...props}></h3>;
}
