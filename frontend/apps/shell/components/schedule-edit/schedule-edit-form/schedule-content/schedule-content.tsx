import { CREATE } from '@/constants/schedule/edit-ui';
import { Textarea, Label } from '@amadda/external-temporal';
import { useScheduleEditStore } from '@/store/schedule/schedule-create/use-schedule-edit-store';
import { useShallow } from 'zustand/react/shallow';

export function ScheduleContent() {
  const [values, handleChange] = useScheduleEditStore(useShallow(state => [state.values, state.handleChange]));

  return (
    <Label htmlFor="scheduleContent">
      <Textarea
        id="scheduleContent"
        name="scheduleContent"
        height="10rem"
        value={values.scheduleContent}
        onChange={handleChange}
        placeholder={CREATE.PLACEHOLDERS.DETAIL}
      />
    </Label>
  );
}
