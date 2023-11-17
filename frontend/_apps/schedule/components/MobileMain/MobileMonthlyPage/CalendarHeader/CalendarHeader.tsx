import { useDateStore } from '@SCH/store/dateStore';
import { useScheduleListStore } from '@SCH/store/scheduleListStore';
import { BtnRound, Filter, Flex, H2, P, Spacing } from 'external-temporal';
import { useState } from 'react';

export function CalendarHeader() {
  const [isOpen, setIsOpen] = useState(false);
  const { year, month, date } = useDateStore();

  const filterOpen = () => {
    setIsOpen(!isOpen);
  };

  return (
    <>
      <Flex justifyContents="spaceBetween" alignItems="center">
        <P color="grey">{year}</P>
        <BtnRound
          type="button"
          variant="key"
          disabled={false}
          onClick={() => {}}
        >
          오늘
        </BtnRound>
      </Flex>
      <Spacing dir="v" size="0.5" />
      <Flex justifyContents="spaceBetween">
        <H2>{month}월</H2>
        <Filter isOpen={isOpen} onClick={filterOpen} main="카테고리">
          <Filter.FilterOption
            id="1"
            name="카테고리1"
            onChange={() => {}}
            value="업무"
          >
            업무
          </Filter.FilterOption>
          <Filter.FilterOption
            id="2"
            name="카테고리2"
            onChange={() => {}}
            value="ssafy"
          >
            싸피
          </Filter.FilterOption>
        </Filter>
      </Flex>
    </>
  );
}
