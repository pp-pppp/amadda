import { BASE } from './Tooltip.css';
import { P } from '#/components/atom/Text/P';

export interface TooltipProps {
  children: string;
}
export function Tooltip({ children }: TooltipProps) {
  return (
    <div className={BASE}>
      <P>{children}</P>
    </div>
  );
}
