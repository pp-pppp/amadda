import { useDateStore } from '@/store/schedule/date-store';
import { useEffect, useState } from 'react';
import { Spacing, Span } from '@amadda/external-temporal';
import { BASE } from './DailyList.css';
import CALENDAR from '@/constants/schedule/CALENDAR';
import { useDailySchedule } from '@/hooks/schedule/use-schedule-list';
import { useCategoryStore } from '@/store/schedule/category-store';
import { MobileDailyPlateList } from '@/components/mobile-daily-plate-list/mobile-daily-plate-list';
import { MobileDailyPlate } from '@/components/mobile-daily-plate/mobile-daily-plate';

export function DailyList() {
  const { selectedCategorySeq, selectedAll, selectedNone } = useCategoryStore();
  const { selectedYear, selectedMonth, selectedDate } = useDateStore();
  const { dailyScheduleList, setDailyScheduleList, SWRerror: error } = useDailySchedule();
  const [profileImages, setProfileImages] = useState<string[]>([]);

  useEffect(() => {
    dailyScheduleList.forEach((schedule, idx) => {
      schedule.participants.forEach(participant => {
        setProfileImages(profileImages => [...profileImages, participant.imageUrl]);
      });
    });
  }, [selectedDate]);

  return (
    <div className={BASE}>
      <MobileDailyPlateList>
        {dailyScheduleList.length === 0 ? (
          <Span color="grey">{CALENDAR.NO_PLAN}</Span>
        ) : (
          dailyScheduleList
            .filter(schedule => selectedCategorySeq.includes(schedule.category.categorySeq) || selectedAll)
            .map((schedule, idx) => {
              return (
                <div key={idx}>
                  <MobileDailyPlate MobileDailyPlateProps={schedule} />
                  <Spacing dir="v" size="0.5" />
                </div>
              );
            })
        )}
      </MobileDailyPlateList>
    </div>
  );
}
