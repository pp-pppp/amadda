import { FABLAYOUT_MOBILE, INDEXLAYOUT_MOBILE } from '@SCH/layout/Layout.css';
import { FAB, Segments } from 'external-temporal';
import { MobileMonthlyPage } from './MobileMonthlyPage/MobileMonthlyPage';
import { MobileUnscheduledPage } from './MobileUnscheduledPage/MobileUnscheduledPage';
import CALENDAR from '@SCH/constants/CALENDAR';

export function MobileMain() {
  return (
    <div className={INDEXLAYOUT_MOBILE}>
      <Segments
        titles={[CALENDAR.TITLE_MONTHLY, CALENDAR.TITLE_UNSCHEUDLED]}
        pages={[<MobileMonthlyPage />, <MobileUnscheduledPage />]}
      />
      <div className={FABLAYOUT_MOBILE}>
        <FAB />
      </div>
    </div>
  );
}
