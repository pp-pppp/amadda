import { MobileDailyPlate } from '@SCH/components/MobileDailyPlate/MobileDailyPlate';
import { MobileDailyPlateList } from '@SCH/components/MobileDailyPlateList/MobileDailyPlateList';
import { useDateStore } from '@SCH/store/dateStore';
import { useScheduleListStore } from '@SCH/store/scheduleListStore';
import { useEffect } from 'react';
import { Spacing, Span } from '../../../../../../_packages/@external-temporal';
import { BASE } from './DailyList.css';

export function DailyList() {
  const { year, month, date } = useDateStore();
  const { schedules } = useScheduleListStore();
  // dummy
  const scheduleList = {
    '2023-11-01': ['일정', '일정', '일정', '일정', '일정'],
    '2023-11-02': ['일정', '일정'],
    '2023-11-04': ['일정', '일정'],
    '2023-11-09': ['일정', '일정'],
    '2023-11-15': ['일정', '일정'],
    '2023-11-16': ['일정', '일정'],
    '2023-11-17': ['일정', '일정'],
    '2023-11-21': ['일정', '일정'],
    '2023-11-30': ['일정', '일정'],
  };

  useEffect(() => {
    useScheduleListStore.setState(state => ({
      schedules: scheduleList,
    }));
  }, []);

  return (
    <div className={BASE}>
      <MobileDailyPlateList>
        {`${year}-${month}-${date}` in schedules ? (
          scheduleList[`${year}-${month}-${date}`].map((content, idx) => (
            <div key={idx}>
              <MobileDailyPlate
                color="grey"
                scheduleName={content}
                person={7}
                isDateSelected={true}
              />
              <Spacing dir="v" size="0.5" />
            </div>
          ))
        ) : (
          <Span color="grey">일정이 없어요.</Span>
        )}
      </MobileDailyPlateList>
    </div>
  );
}
