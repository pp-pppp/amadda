import { CREATE } from '@/constants/schedule/edit';
import { Input, Label } from '@amadda/external-temporal';
import { useScheduleEditStore } from '@/store/schedule/schedule-create/use-schedule-edit-store';
import { useShallow } from 'zustand/react/shallow';

export function ScheduleName() {
  const [values, handleChange] = useScheduleEditStore(useShallow(state => [state.values, state.handleChange]));

  return (
    <Label htmlFor="scheduleName">
      <Input id="scheduleName" type="text" name="scheduleName" value={values.scheduleName} onChange={handleChange} placeholder={CREATE.PLACEHOLDERS.TITLE} />
    </Label>
  );
}
