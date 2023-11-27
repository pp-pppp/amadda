import { FABLAYOUT_MOBILE, INDEXLAYOUT_MOBILE } from '@SCH/layout/Layout.css';
import { FAB, Segments } from 'external-temporal';
import { MobileMonthlyPage } from './MobileMonthlyPage/MobileMonthlyPage';
import { MobileUnscheduledPage } from './MobileUnscheduledPage/MobileUnscheduledPage';
import CALENDAR from '@SCH/constants/CALENDAR';
import { useRouter } from 'next/router';

export function MobileMain() {
  const router = useRouter();

  return (
    <div className={INDEXLAYOUT_MOBILE}>
      <Segments
        titles={[CALENDAR.TITLE_MONTHLY, CALENDAR.TITLE_UNSCHEUDLED]}
        pages={[<MobileMonthlyPage />, <MobileUnscheduledPage />]}
      />
      <div
        className={FABLAYOUT_MOBILE}
        onClick={() =>
          router.push(`${process.env.NEXT_PUBLIC_SHELL}/schedule/create`)
        }
      >
        <FAB />
      </div>
    </div>
  );
}
