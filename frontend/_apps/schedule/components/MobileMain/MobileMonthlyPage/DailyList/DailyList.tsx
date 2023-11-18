import { MobileDailyPlate } from '@SCH/components/MobileDailyPlate/MobileDailyPlate';
import { MobileDailyPlateList } from '@SCH/components/MobileDailyPlateList/MobileDailyPlateList';
import { useDateStore } from '@SCH/store/dateStore';
import { useEffect, useState } from 'react';
import { Spacing, Span } from '../../../../../../_packages/@external-temporal';
import { BASE } from './DailyList.css';
import CALENDAR from '@SCH/constants/CALENDAR';
import { useDailySchedule } from '@SCH/hooks/useScheduleList';
import { toLower } from '@SCH/utils/convertColors';

export function DailyList() {
  const { selectedYear, selectedMonth, selectedDate } = useDateStore();
  const {
    dailyScheduleList,
    setDailyScheduleList,
    SWRerror: error,
  } = useDailySchedule();
  const [profileImages, setProfileImages] = useState<string[]>([]);

  useEffect(() => {
    dailyScheduleList.forEach((schedule, idx) => {
      schedule.participants.forEach(participant => {
        setProfileImages(profileImages => [
          ...profileImages,
          participant.imageUrl,
        ]);
      });
    });
  }, [dailyScheduleList]);

  return (
    <div className={BASE}>
      <MobileDailyPlateList>
        {dailyScheduleList.length === 0 ? (
          <Span color="grey">{CALENDAR.NO_PLAN}</Span>
        ) : (
          dailyScheduleList.map((schedule, idx) => (
            <div key={idx}>
              <MobileDailyPlate
                color={toLower[schedule.category.categoryColor]}
                scheduleName={schedule.scheduleName}
                participants={profileImages}
                person={schedule.participants.length}
                isTimeSelected={schedule.isTimeSelected}
                isDateSelected={schedule.isDateSelected}
                isAllday={schedule.isTimeSelected}
                scheduleStartAt={schedule.scheduleStartAt}
                scheduleEndAt={schedule.scheduleEndAt}
              />
              <Spacing dir="v" size="0.5" />
            </div>
          ))
        )}
      </MobileDailyPlateList>
    </div>
  );
}
