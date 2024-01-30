import { CREATE } from '@SCH/constants/CREATE';
import { useScheduleEditStore } from '@SCH/store/schedule-create/useScheduleEditStore';
import { useShallow } from 'zustand/react/shallow';
import { Input, Label } from '@amadda/external-temporal';

export function ScheduleName() {
  const [values, handleChange] = useScheduleEditStore(useShallow(state => [state.values, state.handleChange, state.refs]));

  return (
    <Label htmlFor="ScheduleName">
      <Input id="scheduleName" type="number" name="scheduleName" value={values.scheduleName} onChange={handleChange} placeholder={CREATE.PLACEHOLDERS.TITLE} />
    </Label>
  );
}
