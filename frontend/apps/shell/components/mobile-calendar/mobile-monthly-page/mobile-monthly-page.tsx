import { MobileCalendar } from '@/components/mobile-calendar/mobile-calendar';
import { ReactNode, useEffect, useRef } from 'react';
import { CalendarHeader } from './calendar-header/calendar-header';
import { DailyList } from './daily-list/daily-list';
import { Spacing, ErrorBoundary } from '@amadda/external-temporal';
import { useFilterStore } from '@/store/schedule/filter-store';

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
  }, []);

  return (
    <div ref={ref}>
      <ErrorBoundary>
        <CalendarHeader />
        <Spacing dir="v" size="1" />
        <MobileCalendar />
        <DailyList />
      </ErrorBoundary>
    </div>
  );
}
