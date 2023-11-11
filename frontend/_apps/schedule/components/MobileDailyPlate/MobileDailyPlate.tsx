import { Flex, Profile, Spacing, Span } from 'external-temporal';
import React from 'react';
import { CATEGORY, LAYOUT, PROFILE } from './MobileDailyPlate.css';
import DAILYPLATE_TEXT from '@SCH/constants/DAILYPLATE_TEXT';

export interface MobileDailyPlateProps {
  color: keyof typeof CATEGORY;
  scheduleName: string;
  person: number;
  participants?: string[];
  unscheduled?: boolean;
  allday?: boolean;
  startTime?: string;
  endTime?: string;
}

export interface PropsObj {
  MobileDailyPlateProps: MobileDailyPlateProps;
}
export function MobileDailyPlate({ MobileDailyPlateProps }: PropsObj) {
  return (
    <div className={LAYOUT.plate}>
      <Flex flexDirection="row" justifyContents="start">
        <div className={CATEGORY[MobileDailyPlateProps.color]} />
        <Spacing size="0.5" dir="h" />
        <Span>{MobileDailyPlateProps.scheduleName}</Span>
      </Flex>
      {MobileDailyPlateProps.person > 1 && (
        <Flex flexDirection="row" justifyContents="end" alignItems="center">
          <Flex
            flexDirection="row"
            justifyContents="center"
            alignItems="center"
          >
            {MobileDailyPlateProps.participants?.map((participant, idx) => (
              <div className={PROFILE[idx]}>
                <Profile src={participant} alt="part" size="small" />
              </div>
            ))}
          </Flex>
          <Spacing size="0.25" dir="h" />
          {MobileDailyPlateProps.person > 3 && (
            <Span>
              {DAILYPLATE_TEXT.PARTICIPANTS(MobileDailyPlateProps.person - 3)}
            </Span>
          )}
        </Flex>
      )}
      <Flex flexDirection="row" justifyContents="start">
        <Spacing size="2" dir="h" />
        {MobileDailyPlateProps.unscheduled && (
          <div className={LAYOUT.width}>
            <Span color="lightgrey">{DAILYPLATE_TEXT.UNSCHEDULED}</Span>
          </div>
        )}
        {MobileDailyPlateProps.allday && (
          <div className={LAYOUT.width}>
            <Span>{DAILYPLATE_TEXT.ALLDAY}</Span>
          </div>
        )}
        {!MobileDailyPlateProps.unscheduled &&
          !MobileDailyPlateProps.allday && (
            <div className={LAYOUT.time}>
              <Span>{MobileDailyPlateProps.startTime}</Span>
              <Span>{MobileDailyPlateProps.endTime}</Span>
            </div>
          )}
      </Flex>
    </div>
  );
}
