import { HTMLAttributes } from 'react';
import { VARIANTS } from './H.css';

interface Props extends HTMLAttributes<HTMLHeadingElement> {}
export default function H5({ ...props }: Props) {
  const className = VARIANTS[5];
  return <h5 className={className} {...props}></h5>;
}
