import { useScheduleEditStore } from '@SCH/store/schedule-create/useScheduleEditStore';
import { Label, Flex, RefInput, Spacing, Span } from '@amadda/external-temporal';

export function IsAuthorizedAll() {
  const [refValues, handleChange, refs] = useScheduleEditStore(state => [state.refValues, state.handleChange, state.refs]);
  return (
    <Label htmlFor="IsAuthorizedAll">
      <Flex justifyContents="start" alignItems="center">
        <RefInput
          ref={refs?.isAuthorizedAll}
          type="checkbox"
          id="isAuthorizedAll"
          name="isAuthorizedAll"
          onChange={e => {
            e.target.value = String(!refValues?.isAuthorizedAll);
            handleChange(e);
          }}
          checked={Boolean(refValues?.isAuthorizedAll)}
          value={String(refValues?.isAuthorizedAll)}
        />
        <Spacing size="0.25" dir="h" />
        <Span color="key">이 일정은 누구나 수정할 수 있어요</Span>
      </Flex>
    </Label>
  );
}
