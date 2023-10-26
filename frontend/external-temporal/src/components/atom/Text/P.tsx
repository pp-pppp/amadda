import { HTMLAttributes } from 'react';

interface Props extends HTMLAttributes<HTMLParagraphElement> {}
export default function P({ ...props }: Props) {
  return <p {...props}></p>;
}
