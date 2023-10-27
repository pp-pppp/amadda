import { Icon } from '@/components/atom/Icon/Icon';
import { BASE } from './FAB.css';

export default function FAB() {
  return (
    <button className={BASE} type="button">
      <Icon type="scheduleadd" color="white" cursor="pointer"></Icon>
    </button>
  );
}
