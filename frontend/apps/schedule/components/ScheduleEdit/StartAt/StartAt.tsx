import { CREATE } from '@SCH/constants/CREATE';
import { useScheduleEditStore } from '@SCH/store/schedule-create/useScheduleEditStore';
import { Flex, Spacing, Input, Span, Label } from '@amadda/external-temporal';

export function StartAt() {
  const [values, handleChange] = useScheduleEditStore(state => [state.values, state.handleChange]);
  return (
    <Label htmlFor="StartAt">
      <Flex justifyContents="start" width="fill">
        <Span>시작 날짜</Span>
        <Spacing dir="h" size="0.5" />
        <Input
          id="startYear"
          type="number"
          name="startYear"
          value={values.startYear}
          onChange={handleChange}
          disabled={values.isDateSelected ? true : false}
          placeholder={CREATE.PLACEHOLDERS.TIME.DATE_START.YY}
        />
        <Spacing dir="h" size="0.25" />
        <Span>/</Span>
        <Spacing dir="h" size="0.25" />
        <Input
          id="startMonth"
          type="number"
          name="startMonth"
          value={values.startMonth}
          onChange={handleChange}
          disabled={values.isDateSelected ? true : false}
          placeholder={CREATE.PLACEHOLDERS.TIME.DATE_START.MM}
        />
        <Spacing dir="h" size="0.25" />
        <Span>/</Span>
        <Spacing dir="h" size="0.25" />
        <Input
          id="startDate"
          type="number"
          name="startDate"
          value={values.startDate}
          onChange={handleChange}
          disabled={values.isDateSelected ? true : false}
          placeholder={CREATE.PLACEHOLDERS.TIME.DATE_START.DD}
        />
      </Flex>
      <Spacing dir="v" size="0.5" />
      <Flex justifyContents="start" width="fill">
        <Span>시작 시간</Span>
        <Spacing dir="h" size="0.5" />
        <Input
          id="startTime"
          type="number"
          name="startTime"
          value={values.startTime}
          onChange={handleChange}
          disabled={values.isAllday || values.isDateSelected ? true : false}
          placeholder={CREATE.PLACEHOLDERS.TIME.DATE_START.TT}
        />
        <Spacing dir="h" size="0.25" />
        <Span>:</Span>
        <Spacing dir="h" size="0.25" />
        <Input
          id="startMinute"
          type="number"
          name="startMinute"
          value={values.startMinute}
          onChange={handleChange}
          disabled={values.isAllday || values.isDateSelected ? true : false}
          placeholder={CREATE.PLACEHOLDERS.TIME.DATE_START.MM}
        />
      </Flex>
    </Label>
  );
}
