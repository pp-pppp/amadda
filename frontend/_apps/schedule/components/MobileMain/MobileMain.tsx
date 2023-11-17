import { FABLAYOUT_MOBILE, INDEXLAYOUT_MOBILE } from '@SCH/layout/Layout.css';
import { FAB, Segments } from 'external-temporal';
import { MobileMonthlyPage } from './MobileMonthlyPage/MobileMonthlyPage';
import { MobileUnscheduledPage } from './MobileUnscheduledPage/MobileUnscheduledPage';

export function MobileMain() {
  return (
    <div className={INDEXLAYOUT_MOBILE}>
      <Segments
        titles={['월간', '미정']}
        pages={[MobileMonthlyPage(), MobileUnscheduledPage()]}
      />
      <div className={FABLAYOUT_MOBILE}>
        <FAB />
      </div>
    </div>
  );
}
