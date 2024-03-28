import { CREATE } from '@/constants/schedule/edit-ui';
import { Flex, Spacing, Input, Span, Label } from '@amadda/external-temporal';
import { useScheduleEditStore } from '@/store/schedule/schedule-create/use-schedule-edit-store';
import { useShallow } from 'zustand/react/shallow';

export function EndAt() {
  const [values, handleChange] = useScheduleEditStore(useShallow(state => [state.values, state.handleChange]));

  return (
    <>
      <Flex justifyContents="start" width="fill">
        <Span>종료 날짜</Span>
        <Spacing dir="h" size="0.5" />
        <Label htmlFor="endYear">
          <Input
            id="endYear"
            type="number"
            name="endYear"
            value={values.endYear}
            onChange={handleChange}
            disabled={values.isDateSelected ? true : false}
            placeholder={CREATE.PLACEHOLDERS.TIME.DATE_END.YY}
          />
        </Label>
        <Spacing dir="h" size="0.25" />
        <Span>/</Span>
        <Spacing dir="h" size="0.25" />
        <Label htmlFor="endMonth">
          <Input
            id="endMonth"
            type="number"
            name="endMonth"
            value={values.endMonth}
            onChange={handleChange}
            disabled={values.isDateSelected ? true : false}
            placeholder={CREATE.PLACEHOLDERS.TIME.DATE_END.MM}
          />
        </Label>
        <Spacing dir="h" size="0.25" />
        <Span>/</Span>
        <Spacing dir="h" size="0.25" />
        <Label htmlFor="endDate">
          <Input
            id="endDate"
            type="number"
            name="endDate"
            value={values.endDate}
            onChange={handleChange}
            disabled={values.isDateSelected ? true : false}
            placeholder={CREATE.PLACEHOLDERS.TIME.DATE_END.DD}
          />
        </Label>
        <Spacing dir="h" size="0.5" />
      </Flex>
      <Spacing dir="v" size="0.5" />
      <Flex justifyContents="start" width="fill">
        <Span>종료 시간</Span>
        <Spacing dir="h" size="0.5" />
        <Label htmlFor="endTime">
          <Input
            id="endTime"
            type="number"
            name="endTime"
            value={values.endTime}
            onChange={handleChange}
            disabled={values.isAllday || values.isDateSelected ? true : false}
            placeholder={CREATE.PLACEHOLDERS.TIME.DATE_END.TT}
          />
        </Label>
        <Spacing dir="h" size="0.25" />
        <Span>:</Span>
        <Spacing dir="h" size="0.25" />
        <Label htmlFor="endMinute">
          <Input
            id="endMinute"
            type="number"
            name="endMinute"
            value={values.endMinute}
            onChange={handleChange}
            disabled={values.isAllday || values.isDateSelected ? true : false}
            placeholder={CREATE.PLACEHOLDERS.TIME.DATE_END.MM}
          />
        </Label>
      </Flex>
    </>
  );
}
