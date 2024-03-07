import { MobileDailyPlate } from '@/components/mobile-daily-plate/MobileDailyPlate';
import { MobileDailyPlateList } from '@/components/mobile-daily-plate-list/MobileDailyPlateList';
import CALENDAR from '@/constants/schedule/CALENDAR';
import { useUnscheduled } from '@/hooks/schedule/use-schedule-list';
import { Spacing, Span, ErrorBoundary } from '@amadda/external-temporal';
import { ReactNode, useEffect, useState } from 'react';

export function MobileUnscheduledPage(): ReactNode {
  const { unscheduledList, setUnscheduledList } = useUnscheduled();
  const [profileImages, setProfileImages] = useState<string[]>([]);

  useEffect(() => {
    unscheduledList.forEach((schedule, idx) => {
      schedule.participants.forEach(participant => {
        setProfileImages(profileImages => [...profileImages, participant.imageUrl]);
      });
    });
  }, [unscheduledList]);

  return (
    <div>
      <ErrorBoundary>
        <MobileDailyPlateList>
          {unscheduledList.length === 0 ? (
            <Span color="grey">{CALENDAR.NO_PLAN}</Span>
          ) : (
            unscheduledList.map((schedule, idx) => (
              <div key={idx}>
                <MobileDailyPlate MobileDailyPlateProps={schedule} type="unscheduled" />
                <Spacing dir="v" size="0.5" />
              </div>
            ))
          )}
        </MobileDailyPlateList>
      </ErrorBoundary>
    </div>
  );
}
