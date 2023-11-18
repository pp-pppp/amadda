import { useDateStore } from '@SCH/store/dateStore';
import { BtnRound, Filter, Flex, H2, P, Spacing } from 'external-temporal';
import { useState } from 'react';
import CALENDAR from '@SCH/constants/CALENDAR';

export function CalendarHeader() {
  const [isOpen, setIsOpen] = useState(false);
  const { selectedYear, selectedMonth, selectedDate } = useDateStore();

  const filterOpen = () => {
    setIsOpen(!isOpen);
  };

  return (
    <>
      <Flex justifyContents="spaceBetween" alignItems="center">
        <P color="grey">{selectedYear}</P>
        <BtnRound
          type="button"
          variant="key"
          disabled={false}
          onClick={() => {}}
        >
          {CALENDAR.TODAY}
        </BtnRound>
      </Flex>
      <Spacing dir="v" size="0.5" />
      <Flex justifyContents="spaceBetween">
        <H2>{selectedMonth}월</H2>
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
