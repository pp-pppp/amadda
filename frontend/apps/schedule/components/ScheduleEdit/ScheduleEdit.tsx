import React, { createContext, useContext } from 'react';
import { Btn, Chip, Flex, Form, H2, H4, Input, Label, Spacing, Span, Textarea, ErrorBoundary, RefInput } from '@amadda/external-temporal';
import useForm from '@amadda/react-util-hooks/useForm';
import { AC, BASE, PARTICIPANTS, SEARCHRESULT } from './ScheduleEdit.css';
import CREATE from '@SCH/constants/CREATE';
import { CategoryContainer } from '../Category/CategoryContainer';
import { initFormValues, refInputNames } from '../../constants/SCHEDULE_EDIT_INIT';
import { useScheduleSubmit } from '../../hooks/useScheduleSubmit';
import { scheduleFormValidator } from '../../utils/validators/scheduleValidator';

export type ScheduleEditProps = Record<string, string> & ReturnType<typeof useForm>;

/**
 * TODO: PUT 상황 대응 리팩토링 (SSR)
 */

export function ScheduleEdit() {
  const data = useForm('scheduleEdit', initFormValues, useScheduleSubmit, scheduleFormValidator, refInputNames);
  const { values, setValues, refValues, invalids, handleChange, refs, submit } = useContext(createContext(data));

  return (
    <ErrorBoundary>
      <div className={BASE}>
        <Spacing size="2" />
        <Form formName="scheduleEdit" onSubmit={submit}>
          <Flex flexDirection="column" justifyContents="start" alignItems="start">
            <H2>{CREATE.ADD_SCHEDULE}</H2>
            {/* 일정 추가 */}
            <Spacing dir="v" size="3" />

            <Label htmlFor="auth">
              <Flex justifyContents="start" alignItems="center">
                <RefInput
                  ref={refs.isAuthorizedAll}
                  type="checkbox"
                  id="isAuthorizedAll"
                  name="isAuthorizedAll"
                  onChange={e => {
                    e.target.value = String(!refValues?.isAuthorizedAll);
                    handleChange(e);
                  }}
                  checked={Boolean(refValues?.isAuthorizedAll)}
                  value={String(refValues?.isAuthorizedAll)}
                />
                <Spacing size="0.25" dir="h" />
                <Span color="key">이 일정은 누구나 수정할 수 있어요</Span>
              </Flex>
            </Label>
            <Spacing dir="v" size="2" />
            {/* 시작 날짜 및 시간 */}
            <Input
              id="scheduleName"
              type="number"
              name="scheduleName"
              value={values.scheduleName}
              onChange={handleChange}
              placeholder={CREATE.PLACEHOLDERS.TITLE}
            />
            <Spacing dir="v" size="2" />
            <Flex justifyContents="start" width="fill">
              <Span>시작 날짜</Span>
              <Spacing dir="h" size="0.5" />
              <Input
                id="startYear"
                type="number"
                name="startYear"
                value={values.startYear}
                onChange={handleChange}
                disabled={values.isDateSelected ? true : false}
                placeholder={CREATE.PLACEHOLDERS.TIME.DATE_START.YY}
              />
              <Spacing dir="h" size="0.25" />
              <Span>/</Span>
              <Spacing dir="h" size="0.25" />
              <Input
                id="startMonth"
                type="number"
                name="startMonth"
                value={values.startMonth}
                onChange={handleChange}
                disabled={values.isDateSelected ? true : false}
                placeholder={CREATE.PLACEHOLDERS.TIME.DATE_START.MM}
              />
              <Spacing dir="h" size="0.25" />
              <Span>/</Span>
              <Spacing dir="h" size="0.25" />
              <Input
                id="startDate"
                type="number"
                name="startDate"
                value={values.startDate}
                onChange={handleChange}
                disabled={values.isDateSelected ? true : false}
                placeholder={CREATE.PLACEHOLDERS.TIME.DATE_START.DD}
              />
            </Flex>
            <Spacing dir="v" size="0.5" />
            <Flex justifyContents="start" width="fill">
              <Span>시작 시간</Span>
              <Spacing dir="h" size="0.5" />
              <Input
                id="startTime"
                type="number"
                name="startTime"
                value={values.startTime}
                onChange={handleChange}
                disabled={values.isAllday || values.isDateSelected ? true : false}
                placeholder={CREATE.PLACEHOLDERS.TIME.DATE_START.TT}
              />
              <Spacing dir="h" size="0.25" />
              <Span>:</Span>
              <Spacing dir="h" size="0.25" />
              <Input
                id="startMinute"
                type="number"
                name="startMinute"
                value={values.startMinute}
                onChange={handleChange}
                disabled={values.isAllday || values.isDateSelected ? true : false}
                placeholder={CREATE.PLACEHOLDERS.TIME.DATE_START.MM}
              />
            </Flex>
            <Spacing dir="v" size="1" />
            {/* 종료 날짜 및 시간 */}
            <Flex justifyContents="start" width="fill">
              <Span>종료 날짜</Span>
              <Spacing dir="h" size="0.5" />
              <Input
                id="endYear"
                type="number"
                name="endYear"
                value={values.endYear}
                onChange={handleChange}
                disabled={values.isAllday || values.isDateSelected ? true : false}
                placeholder={CREATE.PLACEHOLDERS.TIME.DATE_END.YY}
              />
              <Spacing dir="h" size="0.25" />
              <Span>/</Span>
              <Spacing dir="h" size="0.25" />
              <Input
                id="endMonth"
                type="number"
                name="endMonth"
                value={values.endMonth}
                onChange={handleChange}
                disabled={values.isAllday || values.isDateSelected ? true : false}
                placeholder={CREATE.PLACEHOLDERS.TIME.DATE_END.MM}
              />
              <Spacing dir="h" size="0.25" />
              <Span>/</Span>
              <Spacing dir="h" size="0.25" />
              <Input
                id="endDate"
                type="number"
                name="endDate"
                value={values.endDate}
                onChange={handleChange}
                disabled={values.isAllday || values.isDateSelected ? true : false}
                placeholder={CREATE.PLACEHOLDERS.TIME.DATE_END.DD}
              />
              <Spacing dir="h" size="0.5" />
            </Flex>
            <Spacing dir="v" size="0.5" />
            <Flex justifyContents="start" width="fill">
              <Span>종료 시간</Span>
              <Spacing dir="h" size="0.5" />
              <Input
                id="endTime"
                type="number"
                name="endTime"
                value={values.endTime}
                onChange={handleChange}
                disabled={values.isAllday || values.isDateSelected ? true : false}
                placeholder={CREATE.PLACEHOLDERS.TIME.DATE_END.TT}
              />
              <Spacing dir="h" size="0.25" />
              <Span>:</Span>
              <Spacing dir="h" size="0.25" />
              <Input
                id="endMinute"
                type="number"
                name="endMinute"
                value={values.endMinute}
                onChange={handleChange}
                disabled={values.isAllday || values.isDateSelected ? true : false}
                placeholder={CREATE.PLACEHOLDERS.TIME.DATE_END.MM}
              />
            </Flex>
            <Spacing dir="v" />
            {/* 하루 종일 */}
            <Label htmlFor="iad">
              <Flex justifyContents="start" alignItems="center">
                <RefInput
                  ref={refs?.isAllday}
                  type="checkbox"
                  id="isAllday"
                  name="isAllday"
                  onChange={e => {
                    e.target.value = String(!refValues?.isAllday);
                    handleChange(e);
                  }}
                  checked={Boolean(refValues?.isAllday)}
                  value={String(refValues?.isAllday)}
                />
                <Spacing size="0.25" dir="h" />
                <Span color="key">하루 종일</Span>
              </Flex>
            </Label>
            <Spacing dir="v" size="0.5" />
            {/* 날짜 미정 */}
            <Label htmlFor="tbd">
              <Flex justifyContents="start" alignItems="center">
                <RefInput
                  ref={refs?.isDateSelected}
                  type="checkbox"
                  id="isDateSelected"
                  name="isDateSelected"
                  onChange={e => {
                    e.target.value = String(!refValues?.isDateSelected);
                    handleChange(e);
                    if (refValues?.isAllday) setValues({ ...values, isAllday: !refValues?.isAllday });
                  }}
                  checked={Boolean(refValues?.isDateSelected)}
                  value={String(refValues?.isDateSelected)}
                />
                <Spacing size="0.25" dir="h" />
                <Span color="key">날짜를 정하지 않았어요</Span>
              </Flex>
            </Label>
            {/* 친구 할당 */}
            <Spacing dir="v" size="2" />
            {/* 할당된 친구 */}
            <div className={PARTICIPANTS}>
              <Flex flexDirection="row" justifyContents="start">
                {values.participants?.map(p => (
                  <Chip
                    key={p.userId}
                    label={`${p.userName}@${p.userId}`}
                    type="keyword"
                    onDelete={() => {
                      //TODO: 할당 삭제 기능
                    }}
                  />
                ))}
              </Flex>
            </div>
            <Spacing dir="v" size="0.5" />

            {/* 친구 검색 인풋 */}

            <Input
              id="party"
              type="text"
              name="text"
              value={values.partySearchInput}
              onChange={() => {}}
              // onChange={e => fn.partyAutoComplete(e.target.value)}
              onKeyDown={e => {
                if (e.key === 'enter') null;
              }}
              disabled={false}
              placeholder={CREATE.PLACEHOLDERS.PARTY}
            />
            <Spacing dir="v" size="0.5" />
            {/* 자동완성 검색결과 */}
            <div className={SEARCHRESULT}>
              <Flex flexDirection="row" justifyContents="start">
                {values.partySearchResult?.map(group => {
                  if (group.groupName === null)
                    return (
                      <Flex key={group.groupSeq} flexDirection="row" justifyContents="start">
                        {group.groupMember?.map(member => (
                          <div key={member.userId}>
                            <button className={AC} onClick={() => setValues({ ...values, participants: [...values.participants, member] })}>
                              <Chip label={`${member.userName}@${member.userId}`} type="suggestion" />
                            </button>
                            <Spacing dir="h" size="0.5" />
                          </div>
                        ))}
                      </Flex>
                    );
                })}
              </Flex>
            </div>
            <Spacing dir="v" size="2" />
            <Textarea
              id="scheduleContent"
              name="scheduleContent"
              height="10rem"
              value={values.scheduleContent}
              onChange={handleChange}
              placeholder={CREATE.PLACEHOLDERS.DETAIL}
            />
            <Spacing dir="v" size="3" />
            <H4>아래 항목은 나에게만 적용되어요</H4>
            <Spacing dir="v" size="2" />
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
            <Spacing dir="v" size="2" />
            <Span>{CREATE.PLACEHOLDERS.CATEGORY}</Span>
            <Spacing dir="v" size="1" />
            <CategoryContainer categories={values.category} onCategoryClick={() => setValues({ ...values, categorySeq: values.categorySeq })} />
            {/* <Spacing dir="v" size="0.5" />
                TODO: 카테고리 추가 모달 추후 구현
                <div>
                  <Input
                    type="text"
                    id="categoryadd"
                    onChange={e => setvalues.setCategoryInput(e.target.value)}
                    onKeyDown={e => {
                      e.key === 'enter' && fn.postCategory(values.categoryInput);
                      setvalues.setCategoryInput('');
                    }}
                    value={values.categoryInput}
                    name="categoryadd"
                    placeholder="카테고리 추가"
                  />
                </div> */}
            <Spacing dir="v" size="2" />
            <Textarea
              id="scheduleMemo"
              name="scheduleMemo"
              height="10rem"
              value={values.scheduleMemo}
              onChange={handleChange}
              placeholder={CREATE.PLACEHOLDERS.MEMO}
            />
            <Spacing dir="v" size="3" />
            <Btn type="submit" variant="key">
              일정 추가
            </Btn>
          </Flex>
        </Form>
        <Spacing dir="v" size="5" />
      </div>
    </ErrorBoundary>
  );
}
