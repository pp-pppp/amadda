import { useScheduleEditStore } from '@SCH/store/schedule-create/useScheduleEditStore';
import { Label, Flex, Spacing, Span, Input } from '@amadda/external-temporal';
import { useShallow } from 'zustand/react/shallow';

export function IsAuthorizedAll() {
  const [values, handleChange] = useScheduleEditStore(useShallow(state => [state.values, state.handleChange]));

  return (
    <Label htmlFor="isAuthorizedAll">
      <Flex justifyContents="start" alignItems="center">
        <Input type="checkbox" id="isAuthorizedAll" name="isAuthorizedAll" onChange={handleChange} checked={Boolean(values.isAuthorizedAll)} />
        <Spacing size="0.25" dir="h" />
        <Span color="key">이 일정은 누구나 수정할 수 있어요</Span>
      </Flex>
    </Label>
  );
}
