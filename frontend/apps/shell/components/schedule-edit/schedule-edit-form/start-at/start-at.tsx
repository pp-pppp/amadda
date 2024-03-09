import { CREATE } from '@/constants/schedule/CREATE';
import { Flex, Spacing, Input, Span, Label } from '@amadda/external-temporal';
import { useScheduleEditStore } from '@/store/schedule/schedule-create/use-schedule-edit-store';
import { useShallow } from 'zustand/react/shallow';

export function StartAt() {
  const [values, handleChange] = useScheduleEditStore(useShallow(state => [state.values, state.handleChange]));

  return (
    <>
      <Flex justifyContents="start" width="fill">
        <Span>시작 날짜</Span>
        <Spacing dir="h" size="0.5" />
        <Label htmlFor="startYear">
          <Input
            id="startYear"
            type="number"
            name="startYear"
            value={values.startYear}
            onChange={handleChange}
            disabled={values.isDateSelected ? true : false}
            placeholder={CREATE.PLACEHOLDERS.TIME.DATE_START.YY}
          />
        </Label>
        <Spacing dir="h" size="0.25" />
        <Span>/</Span>
        <Spacing dir="h" size="0.25" />{' '}
        <Label htmlFor="startYear">
          <Input
            id="startMonth"
            type="number"
            name="startMonth"
            value={values.startMonth}
            onChange={handleChange}
            disabled={values.isDateSelected ? true : false}
            placeholder={CREATE.PLACEHOLDERS.TIME.DATE_START.MM}
          />
        </Label>
        <Spacing dir="h" size="0.25" />
        <Span>/</Span>
        <Spacing dir="h" size="0.25" />{' '}
        <Label htmlFor="startDate">
          <Input
            id="startDate"
            type="number"
            name="startDate"
            value={values.startDate}
            onChange={handleChange}
            disabled={values.isDateSelected ? true : false}
            placeholder={CREATE.PLACEHOLDERS.TIME.DATE_START.DD}
          />
        </Label>
      </Flex>
      <Spacing dir="v" size="0.5" />
      <Flex justifyContents="start" width="fill">
        <Span>시작 시간</Span>
        <Spacing dir="h" size="0.5" />{' '}
        <Label htmlFor="startTime">
          <Input
            id="startTime"
            type="number"
            name="startTime"
            value={values.startTime}
            onChange={handleChange}
            disabled={values.isAllday || values.isDateSelected ? true : false}
            placeholder={CREATE.PLACEHOLDERS.TIME.DATE_START.TT}
          />
        </Label>
        <Spacing dir="h" size="0.25" />
        <Span>:</Span>
        <Spacing dir="h" size="0.25" />{' '}
        <Label htmlFor="startMinute">
          <Input
            id="startMinute"
            type="number"
            name="startMinute"
            value={values.startMinute}
            onChange={handleChange}
            disabled={values.isAllday || values.isDateSelected ? true : false}
            placeholder={CREATE.PLACEHOLDERS.TIME.DATE_START.MM}
          />
        </Label>
      </Flex>
    </>
  );
}
