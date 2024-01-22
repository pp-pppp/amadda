import CREATE from '@SCH/constants/CREATE';
import { useScheduleEditStore } from '@SCH/store/schedule-create/useScheduleEditStore';
import { Chip, Flex, Spacing, Span } from '@amadda/external-temporal';

export function AlarmTime() {
  const [values, setValues] = useScheduleEditStore(state => [state.values, state.setValues]);

  return (
    <>
      <Span>{CREATE.PLACEHOLDERS.NOTI}</Span>
      <Spacing dir="v" size="0.5" />
      <Flex flexDirection="row" justifyContents="start">
        {Object.keys(CREATE.ALARMS).map(option => (
          <Chip
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
