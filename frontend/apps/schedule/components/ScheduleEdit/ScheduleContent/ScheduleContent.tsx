import CREATE from '@SCH/constants/CREATE';
import { useScheduleEditStore } from '@SCH/store/schedule-create/useScheduleEditStore';
import { Textarea } from '@amadda/external-temporal';

export function ScheduleContent() {
  const [values, handleChange] = useScheduleEditStore(state => [state.values, state.handleChange, state.refs]);

  return (
    <Textarea
      id="scheduleContent"
      name="scheduleContent"
      height="10rem"
      value={values.scheduleContent}
      onChange={handleChange}
      placeholder={CREATE.PLACEHOLDERS.DETAIL}
    />
  );
}
