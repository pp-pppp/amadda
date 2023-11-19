import CREATE from '@SCH/constants/CREATE';
import { stringToNumber } from '@SCH/utils/formatDate';
import {
  CategoryReadResponse,
  FriendReadResponse,
  UserReadResponse,
} from 'amadda-global-types';
import { useRouter } from 'next/router';

import type { Dispatch, ReactNode, SetStateAction } from 'react';
import { useEffect, useState } from 'react';

export interface ScheduleEditDataProps {
  data: {
    startYear: number | undefined;
    setStartYear: Dispatch<SetStateAction<number | undefined>>;
    startMonth: number | undefined;
    setStartMonth: Dispatch<SetStateAction<number | undefined>>;
    startDate: number | undefined;
    setStartDate: Dispatch<SetStateAction<number | undefined>>;
    startTime: number | undefined;
    setStartTime: Dispatch<SetStateAction<number | undefined>>;
    startMinute: number | undefined;
    setStartMinute: Dispatch<SetStateAction<number | undefined>>;
    endYear: number | undefined;
    setEndYear: Dispatch<SetStateAction<number | undefined>>;
    endMonth: number | undefined;
    setEndMonth: Dispatch<SetStateAction<number | undefined>>;
    endDate: number | undefined;
    setEndDate: Dispatch<SetStateAction<number | undefined>>;
    endTime: number | undefined;
    setEndTime: Dispatch<SetStateAction<number | undefined>>;
    endMinute: number | undefined;
    setEndMinute: Dispatch<SetStateAction<number | undefined>>;
    category: Array<CategoryReadResponse>;
    scheduleName: string;
    setScheduleName: Dispatch<SetStateAction<string>>;
    participants: Array<UserReadResponse>;
    setParticipants: Dispatch<SetStateAction<Array<UserReadResponse>>>;
    scheduleStartAt: string;
    setScheduleStartAt: Dispatch<SetStateAction<string>>;
    scheduleEndAt: string;
    setScheduleEndAt: Dispatch<SetStateAction<string>>;
    isDateSelected: boolean;
    setIsDateSelected: Dispatch<SetStateAction<boolean>>;
    isTimeSelected: boolean;
    setIsTimeSelected: Dispatch<SetStateAction<boolean>>;
    isAllday: boolean;
    setIsAllday: Dispatch<SetStateAction<boolean>>;
    isAuthorizedAll: boolean;
    setIsAuthorizedAll: Dispatch<SetStateAction<boolean>>;
    scheduleContent: string;
    setScheduleContent: Dispatch<SetStateAction<string>>;
    alarmTime: keyof typeof CREATE.ALARMS | string;
    setAlarmTime: Dispatch<SetStateAction<keyof typeof CREATE.ALARMS | string>>;
    categorySeq?: number;
    setCategorySeq: Dispatch<SetStateAction<number>>;
    scheduleMemo: string;
    setScheduleMemo: Dispatch<SetStateAction<string>>;
    partyAutoComplete: (key: string) => void;
    partySearchInput: string;
    partySearchResult: FriendReadResponse;
    categoryInput: string;
    setCategoryInput: Dispatch<SetStateAction<string>>;
    addCategory: (key: string) => void;
    onDelete: (userSeq: number) => void;
    submit: () => void;
  };
}
export function ScheduleEditData(props: {
  children: (args: ScheduleEditDataProps) => ReactNode;
}) {
  const router = useRouter();

  const [startYear, setStartYear] = useState<number | undefined>();
  const [startMonth, setStartMonth] = useState<number | undefined>();
  const [startDate, setStartDate] = useState<number | undefined>();
  const [startTime, setStartTime] = useState<number | undefined>();
  const [startMinute, setStartMinute] = useState<number | undefined>();

  const [endYear, setEndYear] = useState<number | undefined>();
  const [endMonth, setEndMonth] = useState<number | undefined>();
  const [endDate, setEndDate] = useState<number | undefined>();
  const [endTime, setEndTime] = useState<number | undefined>();
  const [endMinute, setEndMinute] = useState<number | undefined>();

  const [scheduleName, setScheduleName] = useState<string>('');
  const [participants, setParticipants] = useState<Array<UserReadResponse>>([]);
  const [partySearchInput, setPartySearchInput] = useState<string>('');
  const [partySearchResult, setPartySearchResult] =
    useState<FriendReadResponse>([]);
  const [scheduleStartAt, setScheduleStartAt] = useState<string>('');
  const [scheduleEndAt, setScheduleEndAt] = useState<string>('');
  const [isAuthorizedAll, setIsAuthorizedAll] = useState<boolean>(false);
  const [isDateSelected, setIsDateSelected] = useState<boolean>(false);
  const [isTimeSelected, setIsTimeSelected] = useState<boolean>(false);
  const [isAllday, setIsAllday] = useState<boolean>(false);
  const [scheduleContent, setScheduleContent] = useState<string>('');

  const [alarmTime, setAlarmTime] = useState<
    keyof typeof CREATE.ALARMS | string
  >('NONE');

  const [categorySeq, setCategorySeq] = useState<number | undefined>(undefined);
  const [scheduleMemo, setScheduleMemo] = useState<string>('');

  const [category, setCategory] = useState<Array<CategoryReadResponse>>([]);

  const [categoryInput, setCategoryInput] = useState<string>('');

  const getCategory = () =>
    fetch(`${process.env.NEXT_PUBLIC_USER}/api/user/category`, {
      headers: {
        'Content-Type': 'application/json',
      },
    })
      .then(res => res.json())
      .then(json => setCategory(json.data));

  useEffect(() => {
    getCategory();
  }, [category]);

  if (router.basePath.includes('edit')) {
    const { scheduleSeq } = router.query;
    useEffect(() => {
      try {
        fetch(
          `${process.env.NEXT_PUBLIC_SCHEDULE}/api/schedule/${scheduleSeq}`,
          {
            headers: {
              'Content-Type': 'application/json',
            },
          }
        )
          .then(res => res.json())
          .then(json => json.data)
          .then(data => {
            setScheduleName(data.scheduleName);
            setParticipants(data.participants);
            setScheduleStartAt(data.scheduleStartAt);
            const st: number[] = stringToNumber(data.scheduleStartAt);
            setStartYear(st[0]);
            setStartMonth(st[1]);
            setStartDate(st[2]);
            setStartTime(st[3]);
            setStartMinute(st[4]);
            setScheduleEndAt(data.scheduleEndAt);
            const ed: number[] = stringToNumber(data.scheduleEndAt);
            setEndYear(ed[0]);
            setEndMonth(ed[1]);
            setEndDate(ed[2]);
            setEndTime(ed[3]);
            setEndMinute(ed[4]);
            setIsDateSelected(data.isDateSelected);
            setIsTimeSelected(data.isTimeSelected);
            setIsAllday(data.isAllday);
            setScheduleContent(data.scheduleContent);
            setAlarmTime(data.alarmTime);
            setIsAuthorizedAll(data.isAuthorizedAll);
          });
      } catch (err) {}
    }, [scheduleSeq]);
  }

  const partyAutoComplete = async (key: string) => {
    setPartySearchInput(key);
    return fetch(
      `${process.env.NEXT_PUBLIC_USER}/api/friend?searchKey=${key}`,
      {
        headers: {
          'Content-Type': 'application/json',
        },
      }
    )
      .then(res => res.json())
      .then(json => setPartySearchResult(json.data));
  };

  const addCategory = async (key: string) => {
    fetch(`${process.env.NEXT_PUBLIC_USER}/api/category`, {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json',
      },
    })
      .then(res => res.json())
      .then(json => setPartySearchResult(json.data))
      .then(data => getCategory())
      .catch(err => err);
  };

  const onDelete = (userSeq: number) => {
    const result = participants.filter(p => p.userSeq !== userSeq);
    setParticipants(result);
  };

  const submit = async () => {
    let data;
    if (router.basePath.includes('edit')) {
      const { scheduleSeq } = router.query;
      data = {
        scheduleContent,
        isDateSelected,
        isTimeSelected,
        isAllday,
        isAuthorizedAll,
        scheduleStartAt,
        scheduleEndAt,
        participants,
        alarmTime,
        scheduleName,
        scheduleMemo,
        categorySeq,
      };
      return fetch(
        `${process.env.NEXT_PUBLIC_SCHEDULE}/api/schedule/${scheduleSeq}`,
        {
          method: 'PUT',
          body: data,
          headers: {
            'Content-Type': 'application/json',
          },
        }
      )
        .then(res => res.json())
        .then(json =>
          router.push(`${process.env.NEXT_PUBLIC_SHELL}/schedule/${json.data}`)
        )
        .catch(err =>
          router.push(`${process.env.NEXT_PUBLIC_SHELL}/schedule)`)
        );
    }
    data = {
      scheduleContent,
      isDateSelected,
      isTimeSelected,
      isAllday,
      isAuthorizedAll,
      scheduleStartAt,
      scheduleEndAt,
      participants,
      alarmTime,
      scheduleName,
      scheduleMemo,
    };
    return fetch(`${process.env.NEXT_PUBLIC_SCHEDULE}/api/schedule`, {
      method: 'POST',
      body: data,
      headers: {
        'Content-Type': 'application/json',
      },
    })
      .then(res => res.json())
      .then(json =>
        router.push(`${process.env.NEXT_PUBLIC_SHELL}/schedule/${json.data}`)
      )
      .catch(err => router.push(`${process.env.NEXT_PUBLIC_SHELL}/schedule)`));
  };

  const data = {
    startTime,
    startMonth,
    startDate,
    startMinute,
    startYear,
    setStartDate,
    setStartMinute,
    setStartMonth,
    setStartYear,
    setStartTime,
    setEndDate,
    setEndTime,
    setEndMinute,
    setEndMonth,
    setEndYear,
    endYear,
    endMinute,
    endMonth,
    endTime,
    endDate,
    category,
    scheduleName,
    setScheduleName,
    participants,
    setParticipants,
    scheduleStartAt,
    setScheduleStartAt,
    scheduleEndAt,
    setScheduleEndAt,
    isDateSelected,
    setIsDateSelected,
    isTimeSelected,
    setIsTimeSelected,
    isAllday,
    setIsAllday,
    isAuthorizedAll,
    setIsAuthorizedAll,
    scheduleContent,
    setScheduleContent,
    alarmTime,
    setAlarmTime,
    categorySeq,
    setCategorySeq,
    scheduleMemo,
    setScheduleMemo,
    partyAutoComplete,
    partySearchInput,
    partySearchResult,
    categoryInput,
    setCategoryInput,
    addCategory,
    onDelete,
    submit,
  };
  if (!props.children) return null;

  return props.children({ data });
}
