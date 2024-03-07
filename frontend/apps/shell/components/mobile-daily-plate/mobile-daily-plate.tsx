import { Flex, P, Profile, Spacing, Span } from '@amadda/external-temporal';
import React from 'react';
import { CATEGORY, LAYOUT_SCH, LAYOUT_UN, NAME, PERSON, PROFILE } from './mobile-daily-plate.css';
import DAILYPLATE_TEXT from '@/constants/schedule/DAILYPLATE_TEXT';
import { ScheduleListReadResponse } from '@amadda/global-types';
import { toLower } from '@/utils/schedule/convertColors';
import { useRouter } from 'next/router';

export interface MobileDailyPlateProps {
  MobileDailyPlateProps: ScheduleListReadResponse;
  type?: 'unscheduled' | 'scheduled';
}

export function MobileDailyPlate({ MobileDailyPlateProps, type = 'scheduled' }: MobileDailyPlateProps) {
  const router = useRouter();

  return (
    <div
      className={type === 'scheduled' ? LAYOUT_SCH.plate : LAYOUT_UN.plate}
      onClick={() => router.push(`${process.env.NEXT_PUBLIC_SCHEDULE}/schedule/${MobileDailyPlateProps.scheduleSeq}`)}
    >
      <div className={type === 'scheduled' ? LAYOUT_SCH.title : LAYOUT_UN.title}>
        <div className={CATEGORY[toLower[MobileDailyPlateProps.category.categoryColor]]} />
        <Spacing size="0.5" dir="h" />
        <span className={NAME}>{MobileDailyPlateProps.scheduleName}</span>
      </div>
      {MobileDailyPlateProps.participants.length > 0 && (
        <div className={type === 'scheduled' ? LAYOUT_SCH.participants : LAYOUT_UN.participants}>
          <Flex flexDirection="row" justifyContents="start" alignItems="center" flexWrap="nowrap">
            {MobileDailyPlateProps.participants.length === 1 ? (
              <div className={PROFILE[2]}>
                <Profile src={MobileDailyPlateProps.participants[0].imageUrl} alt="part" size="small" />
              </div>
            ) : MobileDailyPlateProps.participants.length === 2 ? (
              <>
                <div className={PROFILE[1]}>
                  <Profile src={MobileDailyPlateProps.participants[0].imageUrl} alt="part" size="small" />
                </div>
                <div className={PROFILE[2]}>
                  <Profile src={MobileDailyPlateProps.participants[1].imageUrl} alt="part" size="small" />
                </div>
              </>
            ) : (
              MobileDailyPlateProps.participants?.slice(0, 3).map((participant, idx) => (
                <div className={PROFILE[idx]}>
                  <Profile src={participant.imageUrl} alt="part" size="small" />
                </div>
              ))
            )}
          </Flex>
          {MobileDailyPlateProps.participants.length > 3 && (
            <span className={PERSON}>{DAILYPLATE_TEXT.PARTICIPANTS(MobileDailyPlateProps.participants.length)}</span>
          )}
        </div>
      )}
      {MobileDailyPlateProps.isDateSelected && (
        <Flex flexDirection="row" justifyContents="flexEnd">
          <Spacing size="2" dir="h" />
          {MobileDailyPlateProps.isAllday ? (
            <div className={LAYOUT_SCH.width}>
              <Span>{DAILYPLATE_TEXT.ALLDAY}</Span>
            </div>
          ) : MobileDailyPlateProps.isTimeSelected ? (
            <div className={LAYOUT_SCH.time}>
              <Span>
                {MobileDailyPlateProps.scheduleStartAt.split(' ')[1].split(':')[0]}:{MobileDailyPlateProps.scheduleStartAt.split(' ')[1].split(':')[1]}
              </Span>
              <Span>
                {MobileDailyPlateProps.scheduleEndAt.split(' ')[1].split(':')[0]}:{MobileDailyPlateProps.scheduleEndAt.split(' ')[1].split(':')[1]}
              </Span>
            </div>
          ) : (
            <div className={LAYOUT_SCH.width}>
              <Span color="lightgrey">{DAILYPLATE_TEXT.UNSCHEDULED}</Span>
            </div>
          )}
        </Flex>
      )}
    </div>
  );
}
