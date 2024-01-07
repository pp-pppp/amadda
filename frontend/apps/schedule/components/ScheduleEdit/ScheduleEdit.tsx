import React from 'react';
import { Btn, Chip, Flex, Form, H2, H4, Input, Label, Spacing, Span, Textarea, ErrorBoundary } from '@amadda/external-temporal';
import { AC, BASE, PARTICIPANTS, SEARCHRESULT } from './ScheduleEdit.css';
import { ScheduleEditData } from './ScheduleEditData';
import CREATE from '@SCH/constants/CREATE';
import { CategoryContainer } from '../Category/CategoryContainer';

export function ScheduleEdit() {
  return (
    <ScheduleEditData>
      {({ states, setStates, fn }) => (
        <ErrorBoundary>
          <div className={BASE}>
            <Spacing size="2" />
            <Form
              formName="wow"
              onSubmit={e => {
                e.preventDefault();
              }}
            >
              <Flex flexDirection="column" justifyContents="start" alignItems="start">
                <H2>{CREATE.ADD_SCHEDULE}</H2>
                {/* 일정 추가 */}
                <Spacing dir="v" size="3" />

                <Label htmlFor="auth">
                  <Flex justifyContents="start" alignItems="center">
                    <Input
                      type="checkbox"
                      id="auth"
                      name="auth"
                      onChange={() => setStates.setIsAuthorizedAll(!states.isAuthorizedAll)}
                      checked={states.isAuthorizedAll}
                      value={'auth'}
                    />
                    <Spacing size="0.25" dir="h" />
                    <Span color="key">이 일정은 누구나 수정할 수 있어요</Span>
                  </Flex>
                </Label>
                <Spacing dir="v" size="2" />
                {/* 시작 날짜 및 시간 */}
                <Input
                  id="title"
                  type="text"
                  name="title"
                  value={states.scheduleName}
                  onChange={e => setStates.setScheduleName(e.target.value)}
                  placeholder={CREATE.PLACEHOLDERS.TITLE}
                />
                <Spacing dir="v" size="2" />
                <Flex justifyContents="start" width="fill">
                  <Span>시작 날짜</Span>
                  <Spacing dir="h" size="0.5" />
                  <Input
                    id="start_year"
                    type="number"
                    name="start_year"
                    value={states.startYear}
                    onChange={e => setStates.setStartYear(Number(e.target.value))}
                    disabled={states.isDateSelected ? true : false}
                    placeholder={CREATE.PLACEHOLDERS.TIME.DATE_START.YY}
                  />
                  <Spacing dir="h" size="0.25" />
                  <Span>/</Span>
                  <Spacing dir="h" size="0.25" />
                  <Input
                    id="start_month"
                    type="number"
                    name="start_month"
                    value={states.startMonth}
                    onChange={e => setStates.setStartMonth(Number(e.target.value))}
                    disabled={states.isDateSelected ? true : false}
                    placeholder={CREATE.PLACEHOLDERS.TIME.DATE_START.MM}
                  />
                  <Spacing dir="h" size="0.25" />
                  <Span>/</Span>
                  <Spacing dir="h" size="0.25" />
                  <Input
                    id="start_date"
                    type="number"
                    name="start_date"
                    value={states.startDate}
                    onChange={e => setStates.setStartDate(Number(e.target.value))}
                    disabled={states.isDateSelected ? true : false}
                    placeholder={CREATE.PLACEHOLDERS.TIME.DATE_START.DD}
                  />
                </Flex>
                <Spacing dir="v" size="0.5" />
                <Flex justifyContents="start" width="fill">
                  <Span>시작 시간</Span>
                  <Spacing dir="h" size="0.5" />
                  <Input
                    id="start_time"
                    type="number"
                    name="start_time"
                    value={states.startTime}
                    onChange={e => setStates.setStartTime(Number(e.target.value))}
                    disabled={states.isAllday || states.isDateSelected ? true : false}
                    placeholder={CREATE.PLACEHOLDERS.TIME.DATE_START.TT}
                  />
                  <Spacing dir="h" size="0.25" />
                  <Span>:</Span>
                  <Spacing dir="h" size="0.25" />
                  <Input
                    id="start_minute"
                    type="number"
                    name="start_minute"
                    value={states.startMinute}
                    onChange={e => setStates.setEndMinute(Number(e.target.value))}
                    disabled={states.isAllday || states.isDateSelected ? true : false}
                    placeholder={CREATE.PLACEHOLDERS.TIME.DATE_START.MM}
                  />
                </Flex>
                <Spacing dir="v" size="1" />
                {/* 종료 날짜 및 시간 */}
                <Flex justifyContents="start" width="fill">
                  <Span>종료 날짜</Span>
                  <Spacing dir="h" size="0.5" />
                  <Input
                    id="end_year"
                    type="number"
                    name="end_year"
                    value={states.endYear}
                    onChange={e => setStates.setEndYear(Number(e.target.value))}
                    disabled={states.isAllday || states.isDateSelected ? true : false}
                    placeholder={CREATE.PLACEHOLDERS.TIME.DATE_END.YY}
                  />
                  <Spacing dir="h" size="0.25" />
                  <Span>/</Span>
                  <Spacing dir="h" size="0.25" />
                  <Input
                    id="end_month"
                    type="number"
                    name="end_month"
                    value={states.endMonth}
                    onChange={e => setStates.setEndMonth(Number(e.target.value))}
                    disabled={states.isAllday || states.isDateSelected ? true : false}
                    placeholder={CREATE.PLACEHOLDERS.TIME.DATE_END.MM}
                  />
                  <Spacing dir="h" size="0.25" />
                  <Span>/</Span>
                  <Spacing dir="h" size="0.25" />
                  <Input
                    id="end_date"
                    type="number"
                    name="end_date"
                    value={states.endDate}
                    onChange={e => setStates.setEndDate(Number(e.target.value))}
                    disabled={states.isAllday || states.isDateSelected ? true : false}
                    placeholder={CREATE.PLACEHOLDERS.TIME.DATE_END.DD}
                  />
                  <Spacing dir="h" size="0.5" />
                </Flex>
                <Spacing dir="v" size="0.5" />
                <Flex justifyContents="start" width="fill">
                  <Span>종료 시간</Span>
                  <Spacing dir="h" size="0.5" />
                  <Input
                    id="end_time"
                    type="number"
                    name="end_time"
                    value={states.endTime}
                    onChange={e => setStates.setEndTime(Number(e.target.value))}
                    disabled={states.isAllday || states.isDateSelected ? true : false}
                    placeholder={CREATE.PLACEHOLDERS.TIME.DATE_END.TT}
                  />
                  <Spacing dir="h" size="0.25" />
                  <Span>:</Span>
                  <Spacing dir="h" size="0.25" />
                  <Input
                    id="end_minute"
                    type="number"
                    name="end_minute"
                    value={states.endMinute}
                    onChange={e => setStates.setEndMinute(Number(e.target.value))}
                    disabled={states.isAllday || states.isDateSelected ? true : false}
                    placeholder={CREATE.PLACEHOLDERS.TIME.DATE_END.MM}
                  />
                </Flex>
                <Spacing dir="v" />
                {/* 하루 종일 */}
                <Label htmlFor="iad">
                  <Flex justifyContents="start" alignItems="center">
                    <Input
                      type="checkbox"
                      id="iad"
                      name="iad"
                      onChange={() => {
                        setStates.setIsAllday(!states.isAllday);
                        if (states.isDateSelected) setStates.setIsDateSelected(false);
                      }}
                      checked={states.isAllday}
                      value={'iad'}
                    />
                    <Spacing size="0.25" dir="h" />
                    <Span color="key">하루 종일</Span>
                  </Flex>
                </Label>
                <Spacing dir="v" size="0.5" />
                {/* 날짜 미정 */}
                <Label htmlFor="tbd">
                  <Flex justifyContents="start" alignItems="center">
                    <Input
                      type="checkbox"
                      id="tbd"
                      name="tbd"
                      onChange={() => {
                        setStates.setIsDateSelected(!states.isDateSelected);
                        if (states.isAllday) setStates.setIsAllday(false);
                      }}
                      checked={states.isDateSelected}
                      value={'tbd'}
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
                    {states.participants?.map(p => (
                      <Chip key={p.userId} label={`${p.userName}@${p.userId}`} type="keyword" onDelete={() => fn.onDelete(p.userSeq)} />
                    ))}
                  </Flex>
                </div>
                <Spacing dir="v" size="0.5" />

                {/* 친구 검색 인풋 */}
                <Input
                  id="party"
                  type="text"
                  name="text"
                  value={states.partySearchInput}
                  onChange={e => fn.partyAutoComplete(e.target.value)}
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
                    {states.partySearchResult?.map(r => {
                      if (r.groupName === null)
                        return (
                          <Flex key={r.groupSeq} flexDirection="row" justifyContents="start">
                            {r.groupMember?.map(m => (
                              <div key={m.userId}>
                                <button className={AC} onClick={() => setStates.setParticipants([...states.participants, m])}>
                                  <Chip label={`${m.userName}@${m.userId}`} type="suggestion" />
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
                  id="detail"
                  height="10rem"
                  value={states.scheduleContent}
                  onChange={e => setStates.setScheduleContent(e.target.value)}
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
                      type={states.alarmTime === option ? 'filterselected' : 'filter'}
                      onFiltered={() => setStates.setAlarmTime(option)}
                      shape="square"
                      label={CREATE.ALARMS[option]}
                    />
                  ))}
                </Flex>
                <Spacing dir="v" size="2" />
                <Span>{CREATE.PLACEHOLDERS.CATEGORY}</Span>
                <Spacing dir="v" size="1" />
                <CategoryContainer categories={states.category} onCategoryClick={setStates.setCategorySeq} />
                {/* <Spacing dir="v" size="0.5" />
                TODO: 카테고리 추가 모달 추후 구현
                <div>
                  <Input
                    type="text"
                    id="categoryadd"
                    onChange={e => setStates.setCategoryInput(e.target.value)}
                    onKeyDown={e => {
                      e.key === 'enter' && fn.postCategory(states.categoryInput);
                      setStates.setCategoryInput('');
                    }}
                    value={states.categoryInput}
                    name="categoryadd"
                    placeholder="카테고리 추가"
                  />
                </div> */}
                <Spacing dir="v" size="2" />
                <Textarea
                  id="memo"
                  height="10rem"
                  value={states.scheduleMemo}
                  onChange={e => setStates.setScheduleMemo(e.target.value)}
                  placeholder={CREATE.PLACEHOLDERS.MEMO}
                />
                <Spacing dir="v" size="3" />
                <Btn type="submit" onClick={fn.submit} variant="key">
                  일정 추가
                </Btn>
              </Flex>
            </Form>
            <Spacing dir="v" size="5" />
          </div>
        </ErrorBoundary>
      )}
    </ScheduleEditData>
  );
}
