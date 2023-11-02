import { BASE } from './FAB.css';
import { Icon } from '#/components/atom/Icon/Icon';

export function FAB() {
  return (
    <button className={BASE} type="button">
      <Icon color="white" cursor="pointer" type="scheduleadd" />
    </button>
  );
}
