import { MobileDailyPlate } from '@SCH/components/MobileDailyPlate/MobileDailyPlate';
import { MobileDailyPlateList } from '@SCH/components/MobileDailyPlateList/MobileDailyPlateList';
import { useDateStore } from '@SCH/store/dateStore';
import { useEffect, useState } from 'react';
import { Spacing, Span } from '../../../../../../_packages/@external-temporal';
import { BASE } from './DailyList.css';
import CALENDAR from '@SCH/constants/CALENDAR';
import { useDailySchedule } from '@SCH/hooks/useScheduleList';
import { toLower, toUpper } from '@SCH/utils/convertColors';
import { useCategoryStore } from '@SCH/store/categoryStore';
import { CATEGORY } from '@SCH/components/MobileDailyPlate/MobileDailyPlate.css';

export function DailyList() {
  const { selectedCategorySeq, selectedAll, selectedNone } = useCategoryStore();
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
  }, [selectedDate]);

  return (
    <div className={BASE}>
      <MobileDailyPlateList>
        {dailyScheduleList.length === 0 ? (
          <Span color="grey">{CALENDAR.NO_PLAN}</Span>
        ) : (
          dailyScheduleList
            .filter(
              schedule =>
                selectedCategorySeq.includes(schedule.category.categorySeq) ||
                selectedAll
            )
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
