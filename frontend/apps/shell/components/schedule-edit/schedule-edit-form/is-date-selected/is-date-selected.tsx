import { Label, Flex, Input, Spacing, Span } from '@amadda/external-temporal';
import { DATE_INIT } from '@/constants/schedule/date-init';
import { useScheduleEditStore } from '@/store/schedule/schedule-create/use-schedule-edit-store';
import { useShallow } from 'zustand/react/shallow';

export function IsDateSelected() {
  const [values, setValues, handleChange] = useScheduleEditStore(useShallow(state => [state.values, state.setValues, state.handleChange]));

  const handleIsDateSelected = e => {
    handleChange(e);
    if (values.isAllday) {
      setValues({ ...values, isAllday: false, isDateSelected: !values.isDateSelected, ...DATE_INIT });
    }
    console.log(values);
  };

  return (
    <Label htmlFor="isDateSelected">
      <Flex justifyContents="start" alignItems="center">
        <Input type="checkbox" id="isDateSelected" name="isDateSelected" onChange={handleIsDateSelected} checked={values?.isDateSelected} />
        <Spacing size="0.25" dir="h" />
        <Span color="key">날짜를 정하지 않았어요</Span>
      </Flex>
    </Label>
  );
}
