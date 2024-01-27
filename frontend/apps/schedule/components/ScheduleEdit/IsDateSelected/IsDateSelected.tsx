import { useScheduleEditStore } from '@SCH/store/schedule-create/useScheduleEditStore';
import { useShallow } from 'zustand/react/shallow';
import { Label, Flex, Input, Spacing, Span } from '@amadda/external-temporal';

export function IsDateSelected() {
  const [values, setValues, handleChange, refs] = useScheduleEditStore(useShallow(state => [state.values, state.setValues, state.handleChange, state.refs]));
  return (
    <Label htmlFor="IsDateSelected">
      <Flex justifyContents="start" alignItems="center">
        <Input
          type="checkbox"
          id="isDateSelected"
          name="isDateSelected"
          onChange={e => {
            e.target.value = String(!values?.isDateSelected);
            handleChange(e);
            if (values?.isAllday) setValues({ ...values, isAllday: !values?.isAllday });
          }}
          checked={Boolean(values?.isDateSelected)}
          value={String(values?.isDateSelected)}
        />
        <Spacing size="0.25" dir="h" />
        <Span color="key">날짜를 정하지 않았어요</Span>
      </Flex>
    </Label>
  );
}
