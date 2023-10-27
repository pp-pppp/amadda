import { Icon } from '@/components/atom/Icon/Icon';
import { HTMLAttributes } from 'react';
import { BASE } from './FAB.css';

interface Props extends HTMLAttributes<HTMLButtonElement> {
  onClick: () => void;
}

export default function FAB({ onClick }: Props) {
  return (
    <button className={BASE} onClick={onClick}>
      <Icon type="scheduleadd" color="white" cursor="pointer"></Icon>
    </button>
  );
}
