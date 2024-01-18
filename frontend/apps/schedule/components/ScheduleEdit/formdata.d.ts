import { CategoryReadResponse, FriendReadResponse, UserReadResponse } from '@amadda/global-types';

export interface ScheduleEditFormProps {
  startYear: string;
  startMonth: string;
  startDate: string;
  startTime: string;
  startMinute: string;
  endYear: string;
  endMonth: string;
  endDate: string;
  endTime: string;
  endMinute: string;
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
  categorySeq?: string;
  scheduleMemo: string;
  partySearchInput: string;
  partySearchResult: FriendReadResponse;
  categoryInput: string;

  // setStates: {
  //   setStartYear: Dispatch<SetStateAction<string>>;
  //   setStartMonth: Dispatch<SetStateAction<string>>;
  //   setStartDate: Dispatch<SetStateAction<string>>;
  //   setStartTime: Dispatch<SetStateAction<string>>;
  //   setStartMinute: Dispatch<SetStateAction<string>>;
  //   setEndYear: Dispatch<SetStateAction<string>>;
  //   setEndMonth: Dispatch<SetStateAction<string>>;
  //   setEndDate: Dispatch<SetStateAction<string>>;
  //   setEndTime: Dispatch<SetStateAction<string>>;
  //   setEndMinute: Dispatch<SetStateAction<string>>;
  //   setScheduleName: Dispatch<SetStateAction<string>>;
  //   setParticipants: Dispatch<SetStateAction<Array<UserReadResponse>>>;
  //   setScheduleStartAt: Dispatch<SetStateAction<string>>;
  //   setScheduleEndAt: Dispatch<SetStateAction<string>>;
  //   setIsDateSelected: Dispatch<SetStateAction<boolean>>;
  //   setIsTimeSelected: Dispatch<SetStateAction<boolean>>;
  //   setIsAllday: Dispatch<SetStateAction<boolean>>;
  //   setIsAuthorizedAll: Dispatch<SetStateAction<boolean>>;
  //   setScheduleContent: Dispatch<SetStateAction<string>>;
  //   setAlarmTime: Dispatch<SetStateAction<keyof typeof CREATE.ALARMS | string>>;
  //   setCategorySeq: Dispatch<SetStateAction<string>>;
  //   setScheduleMemo: Dispatch<SetStateAction<string>>;
  //   setCategoryInput: Dispatch<SetStateAction<string>>;
  // };
  // fn: {
  //   partyAutoComplete: (key: string) => void;
  //   postCategory: (key: string) => void;
  //   onDelete: (userSeq: string) => void;
  //   submit: () => void;
  // };
}

export interface ScheduleEditDataProps2 {
  states: {
    startYear: string;
    startMonth: string;
    startDate: string;
    startTime: string;
    startMinute: string;
    endYear: string;
    endMonth: string;
    endDate: string;
    endTime: string;
    endMinute: string;
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
    categorySeq?: string;
    scheduleMemo: string;
    partySearchInput: string;
    partySearchResult: FriendReadResponse;
    categoryInput: string;
  };
  setStates: {
    setStartYear: Dispatch<SetStateAction<string>>;
    setStartMonth: Dispatch<SetStateAction<string>>;
    setStartDate: Dispatch<SetStateAction<string>>;
    setStartTime: Dispatch<SetStateAction<string>>;
    setStartMinute: Dispatch<SetStateAction<string>>;
    setEndYear: Dispatch<SetStateAction<string>>;
    setEndMonth: Dispatch<SetStateAction<string>>;
    setEndDate: Dispatch<SetStateAction<string>>;
    setEndTime: Dispatch<SetStateAction<string>>;
    setEndMinute: Dispatch<SetStateAction<string>>;
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
    setCategorySeq: Dispatch<SetStateAction<string>>;
    setScheduleMemo: Dispatch<SetStateAction<string>>;
    setCategoryInput: Dispatch<SetStateAction<string>>;
  };
  fn: {
    partyAutoComplete: (key: string) => void;
    postCategory: (key: string) => void;
    onDelete: (userSeq: string) => void;
    submit: () => void;
  };
}
