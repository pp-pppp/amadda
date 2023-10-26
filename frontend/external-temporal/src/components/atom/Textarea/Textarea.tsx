import { HTMLAttributes } from 'react';
import { SIZE } from './Textarea.css';

interface Props extends HTMLAttributes<HTMLTextAreaElement> {
  value: string;
  placeholder: string;
  height: '5rem' | '7.5rem' | '10rem' | '15rem';
}

export default function Textarea({ value, placeholder, height }: Props) {
  return (
    <textarea
      className={SIZE[height]}
      value={value}
      placeholder={placeholder}
      wrap="on"
    ></textarea>
  );
}
