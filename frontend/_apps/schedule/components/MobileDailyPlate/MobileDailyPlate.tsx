import { Flex, Profile, Spacing, Span } from 'external-temporal';
import React from 'react';
import { CATEGORY, LAYOUT, NAME, PROFILE } from './MobileDailyPlate.css';
import DAILYPLATE_TEXT from '@SCH/constants/DAILYPLATE_TEXT';

export interface MobileDailyPlateProps {
  color: keyof typeof CATEGORY;
  scheduleName: string;
  person: number;
  participants?: string[];
  isTimeSelected?: boolean;
  isDateSelected?: boolean;
  isAllday?: boolean;
  scheduleStartAt?: string; //"yyyy-MM-dd hh:mm:ss",
  scheduleEndAt?: string; //"yyyy-MM-dd hh:mm:ss",
}

export interface PropsObj {
  MobileDailyPlateProps: MobileDailyPlateProps;
}

export function MobileDailyPlate({
  color,
  scheduleName,
  person,
  participants = [],
  isTimeSelected = false,
  isDateSelected = false,
  isAllday = false,
  scheduleStartAt = '',
  scheduleEndAt = '',
}: MobileDailyPlateProps) {
  return (
    <div className={LAYOUT.plate}>
      <div className={LAYOUT.title}>
        <div className={CATEGORY[color]} />
        <Spacing size="0.5" dir="h" />
        <span className={NAME}>{scheduleName}</span>
      </div>
      {person > 1 && (
        <Flex flexDirection="row" justifyContents="flexEnd" alignItems="center">
          <Flex
            flexDirection="row"
            justifyContents="center"
            alignItems="center"
          >
            {participants?.map((participant, idx) => (
              <div className={PROFILE[idx]}>
                <Profile src={participant} alt="part" size="small" />
              </div>
            ))}
          </Flex>
          <Spacing size="0.25" dir="h" />
          {person > 3 && (
            <Span>{DAILYPLATE_TEXT.PARTICIPANTS(person - 3)}</Span>
          )}
        </Flex>
      )}
      {isDateSelected && (
        <Flex flexDirection="row" justifyContents="flexEnd">
          <Spacing size="2" dir="h" />
          {isAllday ? (
            <div className={LAYOUT.width}>
              <Span>{DAILYPLATE_TEXT.ALLDAY}</Span>
            </div>
          ) : isTimeSelected ? (
            <div className={LAYOUT.width}>
              <Span color="lightgrey">{DAILYPLATE_TEXT.UNSCHEDULED}</Span>
            </div>
          ) : (
            <div className={LAYOUT.time}>
              <Span>{scheduleStartAt}</Span>
              <Span>{scheduleEndAt}</Span>
            </div>
          )}
        </Flex>
      )}
    </div>
  );
}
