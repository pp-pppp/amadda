import { CREATE } from '@SCH/constants/CREATE';
import { Label, Textarea } from '@amadda/external-temporal';
import { useContext } from 'react';
import { ScheduleFormContext } from '../ScheduleEditForm';

export function ScheduleMemo() {
  const { values, handleChange } = useContext(ScheduleFormContext);
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
