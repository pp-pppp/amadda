import React from 'react';
import { useState } from 'react';
import { Input } from '#/components/atom/Input/Input';
import { CAL, DATE, DATES, DAYS, FRAME, SELECTED } from './CalendarInput.css';
import { Spacing } from '#/components/atom/Spacing/Spacing';
import { Span } from '#/components/atom/Text/Span';
import { COLOR } from '#/components/atom/Text/Texts.css';
import { Icon } from '#/components/atom/Icon/Icon';
import { H4 } from '#/components/atom/Hn/H4';

export interface CalInputProps {
  validator?(target: string): boolean;
  name: string;
  id1: string;
  id2: string;
  onChange(e: React.ChangeEvent<HTMLInputElement>): void;
  disabled: boolean;
  date?: string;
  time?: string;
  children: React.ReactNode;
}

export interface MiniCalProps {
  isShow: boolean;
  selectedDate: string;
}

export function CalendarInput({
  validator = target => true,
  id1,
  id2,
  name,
  onChange = e => console.log(e),
  date = '',
  time = '',
  disabled,
  children,
  ...props
}: CalInputProps) {
  return (
    <div className={FRAME['flex']}>
      <Input
        type="text"
        id={id1}
        name={name}
        onChange={onChange}
        disabled={disabled}
        value={date}
      />
      <Spacing dir="h" size="1" />
      <Input
        type="text"
        id={id2}
        name={name}
        onChange={onChange}
        disabled={disabled}
        value={time}
      />
      {children}
    </div>
  );
}

CalendarInput.MiniCalendar = function MiniCalendar({
  isShow,
  selectedDate,
  ...props
}: MiniCalProps) {
  // selectedDate : yyyy.mm.dd
  const [year, setYear] = useState(parseInt(selectedDate.split('.')[0]));
  const [month, setMonth] = useState(parseInt(selectedDate.split('.')[1]));
  const [date, setDate] = useState(parseInt(selectedDate.split('.')[2]));

  // 현재 연월의 1일 : 0~6 - 일 ~ 월
  const firstForm = new Date(year, month - 1, 1);
  const day = firstForm.getDay() === 7 ? 0 : firstForm.getDay();

  // 현재 연월의 막일
  const lastForm = new Date(year, month, 0);
  const lastDate = lastForm.getDate();

  // 클래스 이름
  const weekday = `${DATE} ${COLOR['black']}`;
  const saturday = `${DATE} ${COLOR['blue']}`;
  const sunday = `${DATE} ${COLOR['warn']}`;
  const weekdaySelected = `${DATE} ${COLOR['white']} ${SELECTED}`;
  const saturdaySelected = `${DATE} ${COLOR['white']} ${SELECTED}`;
  const sundaySelected = `${DATE} ${COLOR['white']} ${SELECTED}`;

  const decreaseMonth = () => {
    if (month - 1 === 0) {
      setMonth(12);
      setYear(year - 1);
    } else {
      setMonth(month - 1);
    }
  };

  const increaseMonth = () => {
    if (month + 1 === 13) {
      setMonth(1);
      setYear(year + 1);
    } else {
      setMonth(month + 1);
    }
  };

  return (
    <div className={FRAME['cal']}>
      <div className={FRAME['header']}>
        <Icon
          type="before"
          color="black"
          onClick={decreaseMonth}
          cursor="pointer"
        />
        <H4>{year.toString()}</H4>.<H4>{month.toString()}</H4>
        <Icon
          type="next"
          color="black"
          onClick={increaseMonth}
          cursor="pointer"
        />
      </div>

      <div className={DAYS}>
        <Span className={sunday}>일</Span>
        <Span className={weekday}>월</Span>
        <Span className={weekday}>화</Span>
        <Span className={weekday}>수</Span>
        <Span className={weekday}>목</Span>
        <Span className={weekday}>금</Span>
        <Span className={saturday}>토</Span>
      </div>
      <div className={DATES}>
        {Array.from({ length: day }, (_, i) => (
          <Span key={i}> </Span>
        ))}

        {Array.from({ length: lastDate }, (_, i) => {
          let className = date === i + 1 ? weekdaySelected : weekday;

          if ((day + i) % 7 == 6)
            className = date === i + 1 ? saturdaySelected : saturday;
          if ((day + i) % 7 == 0)
            className = date === i + 1 ? sundaySelected : sunday;

          return (
            <Span
              className={className}
              key={i + 1}
              onClick={() => setDate(i + 1)}
            >
              {(i + 1).toString()}
            </Span>
          );
        })}
      </div>
    </div>
  );
};
