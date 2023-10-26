import { HTMLAttributes } from 'react';
import { VARIANTS } from './H.css';

interface Props extends HTMLAttributes<HTMLHeadingElement> {}
export default function H6({ ...props }: Props) {
  const className = VARIANTS[6];
  return <h6 className={className} {...props}></h6>;
}
