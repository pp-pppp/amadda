import { MobileCalendar } from '@SCH/components/MobileCalendar/MobileCalendar';

import { ReactNode, useEffect, useRef } from 'react';
import { CalendarHeader } from './CalendarHeader/CalendarHeader';
import { DailyList } from './DailyList/DailyList';
import { Spacing } from 'external-temporal';
import { useFilterStore } from '@SCH/store/filterStore';

export function MobileMonthlyPage(): ReactNode {
  const { isOpen } = useFilterStore();
  const ref = useRef<HTMLDivElement | null>(null);

  useEffect(() => {
    const filterClose = (e: MouseEvent) => {
      if (ref.current && !ref.current.contains(e.target as Node)) {
        useFilterStore.setState(prevState => ({
          isOpen: false,
        }));
      }
    };

    document.addEventListener('mousedown', filterClose);

    return () => {
      document.removeEventListener('mousedown', filterClose);
    };
  }, [ref]);

  return (
    <div ref={ref}>
      <CalendarHeader />
      <Spacing dir="v" size="1" />
      <MobileCalendar />
      <DailyList />
    </div>
  );
}
