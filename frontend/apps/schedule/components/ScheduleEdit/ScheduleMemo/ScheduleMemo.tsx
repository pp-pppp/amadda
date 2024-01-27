import { CREATE } from '@SCH/constants/CREATE';
import { useScheduleEditStore } from '@SCH/store/schedule-create/useScheduleEditStore';
import { useShallow } from 'zustand/react/shallow';
import { Label, Textarea } from '@amadda/external-temporal';

export function ScheduleMemo() {
  const [values, handleChange] = useScheduleEditStore(useShallow(state => [state.values, state.handleChange, state.refs]));

  return (
    <Label htmlFor="ScheduleMemo">
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
