import { HTMLAttributes } from 'react';
import { VARIANTS } from './H.css';

interface Props extends HTMLAttributes<HTMLHeadingElement> {}
export default function H4({ ...props }: Props) {
  const className = VARIANTS[4];
  return <h4 className={className} {...props}></h4>;
}
