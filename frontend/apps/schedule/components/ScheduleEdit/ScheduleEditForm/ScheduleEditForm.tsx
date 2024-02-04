import React from 'react';
import { Btn, Flex, Form, H2, H4, Spacing } from '@amadda/external-temporal';
import { useForm } from '@amadda/react-util-hooks';
import { CREATE } from '@SCH/constants/CREATE';
import { initFormValues, refInputNames } from '@SCH/constants/SCHEDULE_EDIT_INIT';
import { useScheduleSubmit } from '@SCH/hooks/useScheduleSubmit';
import { scheduleFormValidator } from '@SCH/utils/validators/scheduleValidator';
import { ScheduleEditFormData } from './formdata';
import { IsAuthorizedAll } from './IsAuthorizedAll/IsAuthorizedAll';
import { StartAt } from './StartAt/StartAt';
import { ScheduleName } from './ScheduleName/ScheduleName';
import { EndAt } from './EndAt/EndAt';
import { IsAllDay } from './IsAllDay/IsAllDay';
import { IsDateSelected } from './IsDateSelected/IsDateSelected';
import { Participants } from './Participants/Participants';
import { ScheduleContent } from './ScheduleContent/ScheduleContent';
import { AlarmTime } from './AlarmTime/AlartmTime';
import { Category } from './Category/Category';
import { ScheduleMemo } from './ScheduleMemo/ScheduleMemo';
import { useShallow } from 'zustand/react/shallow';
import { useScheduleEditStore } from '@SCH/store/schedule-create/useScheduleEditStore';

export type ScheduleEditFormProps = {
  scheduleDetail?: ScheduleEditFormData;
};

export function ScheduleEditForm({ scheduleDetail }: ScheduleEditFormProps) {
  const scheduleSubmit = useScheduleSubmit();
  const [values, refValues, submit, setUseFormValues, setUseFormData] = useScheduleEditStore(
    useShallow(state => [state.values, state.refValues, state.submit, state.setUseFormValues, state.setUseFormData])
  );
  useForm<ScheduleEditFormData>({
    initialValues: scheduleDetail || initFormValues,
    onSubmit: scheduleSubmit,
    validator: scheduleFormValidator,
    refInputNames: refInputNames,
    setExternalStoreValues: setUseFormValues,
    setExternalStoreData: setUseFormData,
  });

  return (
    <>
      <Form
        formName="scheduleEdit"
        onSubmit={() => {
          submit({ ...values, ...refValues });
        }}
      >
        <Flex flexDirection="column" justifyContents="start" alignItems="start">
          <H2>{CREATE.ADD_SCHEDULE}</H2>
          <Spacing dir="v" size="3" />
          <IsAuthorizedAll />
          <Spacing dir="v" size="2" />
          <ScheduleName />
          <Spacing dir="v" size="2" />
          <StartAt />
          <Spacing dir="v" size="1" />
          <EndAt />
          <Spacing dir="v" />
          <IsAllDay />
          <Spacing dir="v" size="0.5" />
          <IsDateSelected />
          <Spacing dir="v" size="2" />
          <Participants />
          <Spacing dir="v" size="2" />
          <ScheduleContent />
          <Spacing dir="v" size="3" />
          <H4>{CREATE.ONLY_I_CAN_SEE}</H4>
          <Spacing dir="v" size="2" />
          <AlarmTime />
          <Spacing dir="v" size="2" />
          <Category />
          <ScheduleMemo />
          <Spacing dir="v" size="3" />
          <Btn type="submit" variant="key">
            {CREATE.ADD_SCHEDULE}
          </Btn>
        </Flex>
      </Form>
      <Spacing dir="v" size="5" />
    </>
  );
}
