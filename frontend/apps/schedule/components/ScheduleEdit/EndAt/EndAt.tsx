import { CREATE } from '@SCH/constants/CREATE';
import { useScheduleEditStore } from '@SCH/store/schedule-create/useScheduleEditStore';
import { useShallow } from 'zustand/react/shallow';
import { Flex, Spacing, Input, Span, Label } from '@amadda/external-temporal';

export function EndAt() {
  const [values, handleChange] = useScheduleEditStore(useShallow(state => [state.values, state.handleChange]));
  return (
    <Label htmlFor="EndAt">
      <Flex justifyContents="start" width="fill">
        <Span>종료 날짜</Span>
        <Spacing dir="h" size="0.5" />
        <Input
          id="endYear"
          type="number"
          name="endYear"
          value={values.endYear}
          onChange={handleChange}
          disabled={values.isAllday || values.isDateSelected ? true : false}
          placeholder={CREATE.PLACEHOLDERS.TIME.DATE_END.YY}
        />
        <Spacing dir="h" size="0.25" />
        <Span>/</Span>
        <Spacing dir="h" size="0.25" />
        <Input
          id="endMonth"
          type="number"
          name="endMonth"
          value={values.endMonth}
          onChange={handleChange}
          disabled={values.isAllday || values.isDateSelected ? true : false}
          placeholder={CREATE.PLACEHOLDERS.TIME.DATE_END.MM}
        />
        <Spacing dir="h" size="0.25" />
        <Span>/</Span>
        <Spacing dir="h" size="0.25" />
        <Input
          id="endDate"
          type="number"
          name="endDate"
          value={values.endDate}
          onChange={handleChange}
          disabled={values.isAllday || values.isDateSelected ? true : false}
          placeholder={CREATE.PLACEHOLDERS.TIME.DATE_END.DD}
        />
        <Spacing dir="h" size="0.5" />
      </Flex>
      <Spacing dir="v" size="0.5" />
      <Flex justifyContents="start" width="fill">
        <Span>종료 시간</Span>
        <Spacing dir="h" size="0.5" />
        <Input
          id="endTime"
          type="number"
          name="endTime"
          value={values.endTime}
          onChange={handleChange}
          disabled={values.isAllday || values.isDateSelected ? true : false}
          placeholder={CREATE.PLACEHOLDERS.TIME.DATE_END.TT}
        />
        <Spacing dir="h" size="0.25" />
        <Span>:</Span>
        <Spacing dir="h" size="0.25" />
        <Input
          id="endMinute"
          type="number"
          name="endMinute"
          value={values.endMinute}
          onChange={handleChange}
          disabled={values.isAllday || values.isDateSelected ? true : false}
          placeholder={CREATE.PLACEHOLDERS.TIME.DATE_END.MM}
        />
      </Flex>
    </Label>
  );
}
