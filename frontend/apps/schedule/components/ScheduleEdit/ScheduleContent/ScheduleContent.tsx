import { CREATE } from '@SCH/constants/CREATE';
import { Textarea, Label } from '@amadda/external-temporal';
import { useContext } from 'react';
import { ScheduleFormContext } from '../ScheduleEdit';

export function ScheduleContent() {
  const { values, handleChange } = useContext(ScheduleFormContext);
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
