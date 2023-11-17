import { MobileCalendar } from '@SCH/components/MobileCalendar/MobileCalendar';

import { ReactNode } from 'react';
import { CalendarHeader } from './CalendarHeader/CalendarHeader';
import { DailyList } from './DailyList/DailyList';
import { Spacing } from '../../../../../_packages/@external-temporal';

export function MobileMonthlyPage(): ReactNode {
  return (
    <>
      <CalendarHeader />
      <Spacing dir="v" size="1" />
      <MobileCalendar />
      <DailyList />
    </>
  );
}
