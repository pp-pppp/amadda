import { MobileDailyPlate } from '@SCH/components/MobileDailyPlate/MobileDailyPlate';
import { MobileDailyPlateList } from '@SCH/components/MobileDailyPlateList/MobileDailyPlateList';
import CALENDAR from '@SCH/constants/CALENDAR';
import { useUnscheduled } from '@SCH/hooks/useScheduleList';
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
