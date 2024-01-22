import React from 'react';
import { Btn, Chip, Flex, Form, H2, H4, Input, Label, Spacing, Span, Textarea, ErrorBoundary, RefInput } from '@amadda/external-temporal';
import { useForm } from '@amadda/react-util-hooks';
import { AC, BASE, PARTICIPANTS, SEARCHRESULT } from './ScheduleEdit.css';
import CREATE from '@SCH/constants/CREATE';
import { CategoryContainer } from '../Category/CategoryContainer';
import { initFormValues, refInputNames } from '../../constants/SCHEDULE_EDIT_INIT';
import { useScheduleSubmit } from '../../hooks/useScheduleSubmit';
import { scheduleFormValidator } from '../../utils/validators/scheduleValidator';
import { ScheduleEditFormProps } from './formdata';
import { useScheduleEditStore } from '@SCH/store/schedule-create/useScheduleEditStore';
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

export type ScheduleEditProps = {
  scheduleDetail?: ScheduleEditFormProps;
};

/**
 * TODO: PUT 상황 대응 리팩토링 (SSR)
 */

export function ScheduleEdit({ scheduleDetail }: ScheduleEditProps) {
  const data = useForm<ScheduleEditFormProps>(['scheduleEdit', scheduleDetail || initFormValues, useScheduleSubmit, scheduleFormValidator, refInputNames]);
  const [submit, requestData, setRequestData, setUseFormData] = useScheduleEditStore(state => [
    state.submit,
    state.requestData,
    state.setRequestData,
    state.setUseFormData,
  ]);
  setUseFormData({ data });

  return (
    <ErrorBoundary>
      <div className={BASE}>
        <Spacing size="2" />
        <Form
          formName="scheduleEdit"
          onSubmit={() => {
            setRequestData();
            submit(requestData);
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
      </div>
    </ErrorBoundary>
  );
}
