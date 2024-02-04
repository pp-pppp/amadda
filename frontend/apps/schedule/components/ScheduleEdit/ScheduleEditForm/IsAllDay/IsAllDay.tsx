import { Flex, Input, Label, Spacing, Span } from '@amadda/external-temporal';
import { useScheduleEditStore } from '@SCH/store/schedule-create/useScheduleEditStore';
import { ChangeEvent } from 'react';
import { useShallow } from 'zustand/react/shallow';

export function IsAllDay() {
  const [values, setValues, handleChange] = useScheduleEditStore(useShallow(state => [state.values, state.setValues, state.handleChange]));

  const handleIsAllDay = (e: ChangeEvent<HTMLInputElement>) => {
    handleChange(e);
    if (values?.isDateSelected) setValues({ ...values, isDateSelected: false, isAllday: !values.isAllday });
  };

  return (
    <Label htmlFor="isAllday">
      <Flex justifyContents="start" alignItems="center">
        <Input type="checkbox" id="isAllday" name="isAllday" onChange={handleIsAllDay} checked={values?.isAllday} />
        <Spacing size="0.25" dir="h" />
        <Span color="key">하루 종일</Span>
      </Flex>
    </Label>
  );
}
