import { CategoryReadResponse, FriendReadResponse, UserReadResponse } from 'amadda-global-types';

export interface ScheduleEditDataProps {
  states: {
    startYear: number | undefined;
    startMonth: number | undefined;
    startDate: number | undefined;
    startTime: number | undefined;
    startMinute: number | undefined;
    endYear: number | undefined;
    endMonth: number | undefined;
    endDate: number | undefined;
    endTime: number | undefined;
    endMinute: number | undefined;
    category: Array<CategoryReadResponse>;
    scheduleName: string;
    participants: Array<UserReadResponse>;
    scheduleStartAt: string;
    scheduleEndAt: string;
    isDateSelected: boolean;
    isTimeSelected: boolean;
    isAllday: boolean;
    isAuthorizedAll: boolean;
    scheduleContent: string;
    alarmTime: keyof typeof CREATE.ALARMS | string;
    categorySeq?: number;
    scheduleMemo: string;
    partySearchInput: string;
    partySearchResult: FriendReadResponse;
    categoryInput: string;
  };
  setStates: {
    setStartYear: Dispatch<SetStateAction<number | undefined>>;
    setStartMonth: Dispatch<SetStateAction<number | undefined>>;
    setStartDate: Dispatch<SetStateAction<number | undefined>>;
    setStartTime: Dispatch<SetStateAction<number | undefined>>;
    setStartMinute: Dispatch<SetStateAction<number | undefined>>;
    setEndYear: Dispatch<SetStateAction<number | undefined>>;
    setEndMonth: Dispatch<SetStateAction<number | undefined>>;
    setEndDate: Dispatch<SetStateAction<number | undefined>>;
    setEndTime: Dispatch<SetStateAction<number | undefined>>;
    setEndMinute: Dispatch<SetStateAction<number | undefined>>;
    setScheduleName: Dispatch<SetStateAction<string>>;
    setParticipants: Dispatch<SetStateAction<Array<UserReadResponse>>>;
    setScheduleStartAt: Dispatch<SetStateAction<string>>;
    setScheduleEndAt: Dispatch<SetStateAction<string>>;
    setIsDateSelected: Dispatch<SetStateAction<boolean>>;
    setIsTimeSelected: Dispatch<SetStateAction<boolean>>;
    setIsAllday: Dispatch<SetStateAction<boolean>>;
    setIsAuthorizedAll: Dispatch<SetStateAction<boolean>>;
    setScheduleContent: Dispatch<SetStateAction<string>>;
    setAlarmTime: Dispatch<SetStateAction<keyof typeof CREATE.ALARMS | string>>;
    setCategorySeq: Dispatch<SetStateAction<number>>;
    setScheduleMemo: Dispatch<SetStateAction<string>>;
    setCategoryInput: Dispatch<SetStateAction<string>>;
  };
  fn: {
    partyAutoComplete: (key: string) => void;
    postCategory: (key: string) => void;
    onDelete: (userSeq: number) => void;
    submit: () => void;
  };
}
