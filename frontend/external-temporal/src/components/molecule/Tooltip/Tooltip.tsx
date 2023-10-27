import P from '@/components/atom/Text/P';
import { BASE } from './Tooltip.css';

export default function Tooltip({ ...props }) {
  return (
    <div className={BASE}>
      <P {...props}></P>
    </div>
  );
}
