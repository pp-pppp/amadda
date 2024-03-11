import React from 'react';
import { Btn, Flex, Form, H2, H4, Spacing } from '@amadda/external-temporal';
import { useForm } from '@amadda/react-util-hooks';
import { CREATE } from '@/constants/schedule/edit';
import { initFormValues, refInputNames } from '@/constants/schedule/schedule-edit-init';
import { useScheduleSubmit } from '@/hooks/schedule/use-schedule-submit';
import { scheduleFormValidator } from '@/utils/schedule/validators/scheduleValidator';
import { ScheduleEditFormData } from './formdata';
import { IsAuthorizedAll } from './is-authorized-all/is-authorized-all';
import { StartAt } from './start-at/start-at';
import { ScheduleName } from './schedule-name/schedule-name';
import { EndAt } from './end-at/end-at';
import { IsAllDay } from './is-all-day/is-all-day';
import { IsDateSelected } from './is-date-selected/is-date-selected';
import { Participants } from './participants/participants';
import { ScheduleContent } from './schedule-content/schedule-content';
import { AlarmTime } from './alarm-time/alarm-time';
import { Category } from './category/category';
import { ScheduleMemo } from './schedule-memo/schedule-memo';
import { useShallow } from 'zustand/react/shallow';
import { useScheduleEditStore } from '@/store/schedule/schedule-create/use-schedule-edit-store';

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
