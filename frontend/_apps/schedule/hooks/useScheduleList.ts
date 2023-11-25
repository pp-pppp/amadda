// 한달치 스케줄 리스트를 갖고오는 훅
import useSWR from 'swr';
import { useDateStore } from '@SCH/store/dateStore';
import { http } from '@SCH/utils/http';
import { useEffect, useState } from 'react';
import {
  ScheduleListReadResponse,
  ScheduleSearchResponse,
} from 'amadda-global-types';

export interface ScheduleListType {
  categorySeq?: string | undefined;
  searchKey?: string | undefined;
  unscheduled?: boolean | undefined;
  year?: string | undefined;
  month?: string | undefined;
  day?: string | undefined;
}

const scheduleList = ({
  categorySeq,
  searchKey,
  unscheduled,
  year,
  month,
  day,
}: ScheduleListType) => {
  const params = new URLSearchParams();

  categorySeq && params.append('category', categorySeq);
  searchKey && params.append('searchKey', searchKey);
  unscheduled && params.append('unscheduled', unscheduled.toString());
  year && params.append('year', year);
  month && params.append('month', month);
  day && params.append('day', day);

  return http
    .get<ScheduleSearchResponse>(
      `${process.env.NEXT_PUBLIC_SCHEDULE}/api/schedule${params.toString()}`
    )
    .then(res => res.data);
};

export function useMonthlySchedule() {
  const [monthlyScheduleList, setMonthlyScheduleList] =
    useState<ScheduleSearchResponse>({});
  const { selectedYear, selectedMonth } = useDateStore();
  const { data, error, isLoading } = useSWR('/api/schedule?', () =>
    scheduleList({ year: selectedYear, month: selectedMonth })
  );

  useEffect(() => data && setMonthlyScheduleList(data), [data]);

  return { monthlyScheduleList, setMonthlyScheduleList, SWRerror: error };
}

export function useDailySchedule() {
  const [dailyScheduleList, setDailyScheduleList] = useState<
    Array<ScheduleListReadResponse>
  >([]);
  const { selectedYear, selectedMonth, selectedDate } = useDateStore();

  const { data, error, isLoading } = useSWR('/api/schedule?', () =>
    scheduleList({
      year: selectedYear,
      month: selectedMonth,
      day: selectedDate,
    })
  );

  useEffect(
    () =>
      data &&
      setDailyScheduleList(
        data[`${selectedYear}-${selectedMonth}-${selectedDate}`]
      ),
    [data]
  );

  return { dailyScheduleList, setDailyScheduleList, SWRerror: error };
}

export function useUnscheduled() {
  const [unscheduledList, setUnscheduledList] = useState<
    Array<ScheduleListReadResponse>
  >([]);
  const { data, error, isLoading } = useSWR('/api/schedule?', () =>
    scheduleList({ unscheduled: true })
  );

  useEffect(
    () => data?.unscheduled && setUnscheduledList(data.unscheduled),
    [data]
  );

  return { unscheduledList, setUnscheduledList, SWRerror: error };
}
