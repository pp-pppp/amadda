import { CREATE } from '@/constants/schedule/CREATE';
import { Chip, Flex, Spacing, Span } from '@amadda/external-temporal';
import { useShallow } from 'zustand/react/shallow';
import { useScheduleEditStore } from '@/store/schedule/schedule-create/useScheduleEditStore';

export function AlarmTime() {
  const [values, setValues] = useScheduleEditStore(useShallow(state => [state.values, state.setValues]));

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
