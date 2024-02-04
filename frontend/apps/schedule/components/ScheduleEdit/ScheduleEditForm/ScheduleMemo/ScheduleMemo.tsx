import { CREATE } from '@SCH/constants/CREATE';
import { useScheduleEditStore } from '@SCH/store/schedule-create/useScheduleEditStore';
import { Label, Textarea } from '@amadda/external-temporal';
import { useShallow } from 'zustand/react/shallow';

export function ScheduleMemo() {
  const [values, handleChange] = useScheduleEditStore(useShallow(state => [state.values, state.handleChange]));

  return (
    <Label htmlFor="scheduleMemo">
      <Textarea
        id="scheduleMemo"
        name="scheduleMemo"
        height="10rem"
        value={values.scheduleMemo}
        onChange={handleChange}
        placeholder={CREATE.PLACEHOLDERS.MEMO}
      />
    </Label>
  );
}
