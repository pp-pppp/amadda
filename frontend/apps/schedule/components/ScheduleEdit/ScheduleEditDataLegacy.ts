// import CREATE from '@SCH/constants/CREATE';
// import { useGetCategory, usePostCategory } from '@SCH/hooks/useCategory';
// import { stringTostring } from '@SCH/utils/formatDate';
// import { FriendReadResponse, UserReadResponse, ScheduleCreateRequest } from '@amadda/global-types';
// import { useRouter } from 'next/router';
// import type { Dispatch, ReactNode, SetStateAction } from 'react';
// import { useEffect, useState } from 'react';
// import { ScheduleEditFormProps } from './formdata';

// export function ScheduleEditData(props: { children: (args: ScheduleEditFormProps) => ReactNode }) {
//   const router = useRouter();

//   const [startYear, setStartYear] = useState<string | undefined>();
//   const [startMonth, setStartMonth] = useState<string | undefined>();
//   const [startDate, setStartDate] = useState<string | undefined>();
//   const [startTime, setStartTime] = useState<string | undefined>();
//   const [startMinute, setStartMinute] = useState<string | undefined>();

//   const [endYear, setEndYear] = useState<string | undefined>();
//   const [endMonth, setEndMonth] = useState<string | undefined>();
//   const [endDate, setEndDate] = useState<string | undefined>();
//   const [endTime, setEndTime] = useState<string | undefined>();
//   const [endMinute, setEndMinute] = useState<string | undefined>();

//   const [scheduleName, setScheduleName] = useState<string>('');
//   const [participants, setParticipants] = useState<Array<UserReadResponse>>([]);
//   const [partySearchInput, setPartySearchInput] = useState<string>('');
//   const [partySearchResult, setPartySearchResult] = useState<FriendReadResponse>([]);
//   const [scheduleStartAt, setScheduleStartAt] = useState<string>('');
//   const [scheduleEndAt, setScheduleEndAt] = useState<string>('');
//   const [isAuthorizedAll, setIsAuthorizedAll] = useState<boolean>(false);
//   const [isDateSelected, setIsDateSelected] = useState<boolean>(false);
//   const [isTimeSelected, setIsTimeSelected] = useState<boolean>(false);
//   const [isAllday, setIsAllday] = useState<boolean>(false);
//   const [scheduleContent, setScheduleContent] = useState<string>('');

//   const [alarmTime, setAlarmTime] = useState<keyof typeof CREATE.ALARMS | string>('NONE');

//   const [categorySeq, setCategorySeq] = useState<string | undefined>(undefined);
//   const [scheduleMemo, setScheduleMemo] = useState<string>('');

//   const [categoryInput, setCategoryInput] = useState<string>('');

//   const scheduleInputData = {
//     scheduleName: '',
//     participants: [],
//     scheduleStartAt: '',
//     scheduleEndAt: '',
//     isDateSelected: false,
//     isTimeSelected: false,
//     isAllday: false,
//     isAuthorizedAll: false,
//     scheduleContent: '',
//     alarmTime: 'NONE',
//     scheduleMemo: '',
//   };
//   const [writtenSchedule, setWrittenSchedule] = useState<ScheduleCreateRequest>(scheduleInputData);
//   const { category, categoryIsLoading } = useGetCategory();

//   if (router.basePath.includes('edit')) {
//     const { scheduleSeq } = router.query;
//     useEffect(() => {
//       try {
//         fetch(`${process.env.NEXT_PUBLIC_SCHEDULE}/api/schedule/${scheduleSeq}`, {
//           headers: {
//             'Content-Type': 'application/json',
//           },
//         })
//           .then(res => res.json())
//           .then(json => json.data)
//           .then(data => {
//             setScheduleName(data.scheduleName);
//             setParticipants(data.participants);
//             setScheduleStartAt(data.scheduleStartAt);
//             const st: string[] = stringTostring(data.scheduleStartAt);
//             setStartYear(st[0]);
//             setStartMonth(st[1]);
//             setStartDate(st[2]);
//             setStartTime(st[3]);
//             setStartMinute(st[4]);
//             setScheduleEndAt(data.scheduleEndAt);
//             const ed: string[] = stringTostring(data.scheduleEndAt);
//             setEndYear(ed[0]);
//             setEndMonth(ed[1]);
//             setEndDate(ed[2]);
//             setEndTime(ed[3]);
//             setEndMinute(ed[4]);
//             setIsDateSelected(data.isDateSelected);
//             setIsTimeSelected(data.isTimeSelected);
//             setIsAllday(data.isAllday);
//             setScheduleContent(data.scheduleContent);
//             setAlarmTime(data.alarmTime);
//             setIsAuthorizedAll(data.isAuthorizedAll);
//           });
//       } catch (err) {}
//     }, [scheduleSeq]);
//   }

//   const partyAutoComplete = async (key: string) => {
//     //TODO: SWR Migration
//     setPartySearchInput(key);
//     return fetch(`${process.env.NEXT_PUBLIC_USER}/api/friend?searchKey=${key}`, {
//       headers: {
//         'Content-Type': 'application/json',
//       },
//     })
//       .then(res => res.json())
//       .then(json => setPartySearchResult(json.data));
//   };

//   const postCategory = (categoryName: string) => usePostCategory(categoryName);

//   const onDelete = (userSeq: string) => {
//     const result = participants.filter(p => p.userSeq !== userSeq);
//     setParticipants(result);
//   };

//   const submit = async () => {
//     if (router.basePath.includes('edit')) {
//       const { scheduleSeq } = router.query;

//       return fetch(`${process.env.NEXT_PUBLIC_SCHEDULE}/api/schedule/${scheduleSeq}`, {
//         method: 'PUT',
//         body: JSON.stringify(writtenSchedule),
//         headers: {
//           'Content-Type': 'application/json',
//         },
//       })
//         .then(res => res.json())
//         .then(json => router.push(`${process.env.NEXT_PUBLIC_SHELL}/schedule/${json.data}`))
//         .catch(err => router.push(`${process.env.NEXT_PUBLIC_SHELL}/schedule`));
//     }
//     return fetch(`${process.env.NEXT_PUBLIC_SCHEDULE}/api/schedule`, {
//       method: 'POST',
//       body: JSON.stringify(writtenSchedule),
//       headers: {
//         'Content-Type': 'application/json',
//       },
//     })
//       .then(res => res.json())
//       .then(json => router.push(`${process.env.NEXT_PUBLIC_SHELL}/schedule/${json.data}`))
//       .catch(err => router.push(`${process.env.NEXT_PUBLIC_SHELL}/schedule`));
//   };

//   const states = {
//     startTime,
//     startMonth,
//     startDate,
//     startMinute,
//     startYear,
//     endYear,
//     endMinute,
//     endMonth,
//     endTime,
//     endDate,
//     category,
//     scheduleName,
//     participants,
//     scheduleStartAt,
//     scheduleEndAt,
//     isDateSelected,
//     isTimeSelected,
//     isAllday,
//     isAuthorizedAll,
//     scheduleContent,
//     alarmTime,
//     categorySeq,
//     scheduleMemo,
//     partySearchInput,
//     partySearchResult,
//     categoryInput,
//   };
//   const setStates = {
//     setStartDate,
//     setStartMinute,
//     setStartMonth,
//     setStartYear,
//     setStartTime,
//     setEndDate,
//     setEndTime,
//     setEndMinute,
//     setEndMonth,
//     setEndYear,

//     setScheduleName,
//     setParticipants,
//     setScheduleStartAt,
//     setScheduleEndAt,
//     setIsDateSelected,
//     setIsTimeSelected,
//     setIsAllday,
//     setIsAuthorizedAll,
//     setScheduleContent,
//     setAlarmTime,
//     setCategorySeq,
//     setScheduleMemo,

//     setCategoryInput,
//   };
//   const fn = {
//     partyAutoComplete,
//     postCategory,
//     onDelete,
//     submit,
//   };
//   if (!props.children) return null;

//   return props.children({ states, setStates, fn });
// }
