import { CREATE } from '@SCH/constants/CREATE';
import { Input, Label } from '@amadda/external-temporal';
import { useContext } from 'react';
import { ScheduleFormContext } from '../ScheduleEditForm';

export function ScheduleName() {
  const { values, handleChange } = useContext(ScheduleFormContext);
  return (
    <Label htmlFor="scheduleName">
      <Input id="scheduleName" type="text" name="scheduleName" value={values.scheduleName} onChange={handleChange} placeholder={CREATE.PLACEHOLDERS.TITLE} />
    </Label>
  );
}
