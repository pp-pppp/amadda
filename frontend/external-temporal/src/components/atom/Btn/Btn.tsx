import { HTMLAttributes } from 'react';
import { VARIANTS } from './Btn.css';

interface Props extends HTMLAttributes<HTMLButtonElement> {
  type: 'button' | 'submit';
  variant: keyof typeof VARIANTS;
  disabled: boolean;
  onClick: () => void;
}
export default function Btn({ type, variant, disabled, ...props }: Props) {
  const className = `${VARIANTS[variant]}`;
  return (
    <button
      type={type}
      className={className}
      disabled={disabled}
      {...props}
    ></button>
  );
}
