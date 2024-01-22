import { useScheduleEditStore } from '@SCH/store/schedule-create/useScheduleEditStore';
import { Flex, Input, Label, Spacing, Span } from '@amadda/external-temporal';

export function IsAllDay() {
  const [values, handleChange] = useScheduleEditStore(state => [state.refValues, state.handleChange, state.refs]);

  return (
    <Label htmlFor="IsAllDay">
      <Flex justifyContents="start" alignItems="center">
        <Input
          type="checkbox"
          id="isAllday"
          name="isAllday"
          onChange={e => {
            e.target.value = String(!values?.isAllday);
            handleChange(e);
          }}
          checked={Boolean(values?.isAllday)}
          value={String(values?.isAllday)}
        />
        <Spacing size="0.25" dir="h" />
        <Span color="key">하루 종일</Span>
      </Flex>
    </Label>
  );
}
