import { CREATE } from '@SCH/constants/CREATE';
import { Chip, Flex, Spacing, Span } from '@amadda/external-temporal';
import { useContext } from 'react';
import { ScheduleFormContext } from '../ScheduleEdit';

export function AlarmTime() {
  const { values, setValues } = useContext(ScheduleFormContext);
  return (
    <>
      <Span>{CREATE.PLACEHOLDERS.NOTI}</Span>
      <Spacing dir="v" size="0.5" />
      <Flex flexDirection="row" justifyContents="start">
        {Object.keys(CREATE.ALARMS).map(option => (
          <Chip
            key={option}
            type={values.alarmTime === option ? 'filterselected' : 'filter'}
            onFiltered={() => setValues({ ...values, alarmTime: option })}
            shape="square"
            label={CREATE.ALARMS[option]}
          />
        ))}
      </Flex>
    </>
  );
}
