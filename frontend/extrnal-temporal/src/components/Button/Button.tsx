import { HTMLAttributes } from 'react';
import { base } from './Button.css';
interface Props extends HTMLAttributes<HTMLButtonElement> {
  label: string;
}
export default function Button({ label }: Props) {
  return <button className={base}>{label}</button>;
}
