import { HTMLAttributes } from 'react';
import { SIZE, VARIANTS } from './RBtn.css';

interface Props extends HTMLAttributes<HTMLButtonElement> {
  type: 'button' | 'submit';
  variant: keyof typeof VARIANTS;
  size?: keyof typeof SIZE;
  disabled: boolean;
  onClick: () => void;
}
export default function RBtn({
  type,
  variant,
  disabled,
  size = 'M',
  ...props
}: Props) {
  const className = `${VARIANTS[variant]} ${SIZE[size]}`;
  return (
    <button
      type={type}
      className={className}
      disabled={disabled}
      {...props}
    ></button>
  );
}
