import { HTMLAttributes } from 'react';
import { SIZE } from './Textarea.css';

interface Props extends HTMLAttributes<HTMLTextAreaElement> {
  value: string;
  placeholder: string;
  height: keyof typeof SIZE;
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
