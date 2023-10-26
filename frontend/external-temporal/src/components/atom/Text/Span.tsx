import { HTMLAttributes } from 'react';

interface Props extends HTMLAttributes<HTMLSpanElement> {}
export default function Span({ ...props }: Props) {
  return <span {...props}></span>;
}
