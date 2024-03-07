import React from 'react';
import { FABLAYOUT_MOBILE, INDEXLAYOUT_MOBILE } from '@/layout/layouts.css';
import { FAB, Segments } from '@amadda/external-temporal';
import { MobileMonthlyPage } from './MobileMonthlyPage/MobileMonthlyPage';
import { MobileUnscheduledPage } from './MobileUnscheduledPage/MobileUnscheduledPage';
import CALENDAR from '@/constants/schedule/CALENDAR';
import { useRouter } from 'next/router';

export function MobileMain() {
  const router = useRouter();

  return (
    <>
      <div className={INDEXLAYOUT_MOBILE}>
        <Segments titles={[CALENDAR.TITLE_MONTHLY, CALENDAR.TITLE_UNSCHEUDLED]} pages={[<MobileMonthlyPage />, <MobileUnscheduledPage />]} />
      </div>
      <div className={FABLAYOUT_MOBILE} onClick={() => router.push(`${process.env.NEXT_PUBLIC_SHELL}/schedule/create`)}>
        <FAB />
      </div>
    </>
  );
}
