import { HTMLAttributes } from 'react';
import { base, test } from './Button.css';
interface Props extends HTMLAttributes<HTMLButtonElement> {
  label: string;
}
export default function Button({ label }: Props) {
  return (
    <>
      <button className={base}>{label}</button>
      <button className={test}>{label}</button>
    </>
  );
}
