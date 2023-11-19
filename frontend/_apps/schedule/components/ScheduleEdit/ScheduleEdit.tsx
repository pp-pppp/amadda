import React from 'react';
import {
  Btn,
  Chip,
  Flex,
  Form,
  H2,
  H4,
  Input,
  Label,
  Spacing,
  Span,
  Textarea,
} from 'external-temporal';
import { AC, BASE, PARTICIPANTS, SEARCHRESULT } from './ScheduleEdit.css';
import { ScheduleEditData } from './ScheduleEditData';
import CREATE from '@SCH/constants/CREATE';
import { CATEGORY } from '../MobileDailyPlate/MobileDailyPlate.css';
import { Category } from './Category/Category';
import { Container } from './Category/Container';

export function ScheduleEdit() {
  return (
    <ScheduleEditData>
      {({ data }) => (
        <div className={BASE}>
          <Spacing size="2" />
          <Form
            formName="wow"
            onSubmit={e => {
              e.preventDefault();
            }}
          >
            <Flex
              flexDirection="column"
              justifyContents="start"
              alignItems="start"
            >
              <H2>{CREATE.ADD_SCHEDULE}</H2>
              {/* 일정 추가 */}
              <Spacing dir="v" size="3" />

              <Label htmlFor="auth">
                <Flex justifyContents="start" alignItems="center">
                  <Input
                    type="checkbox"
                    id="auth"
                    name="auth"
                    onChange={() =>
                      data.setIsAuthorizedAll(!data.isAuthorizedAll)
                    }
                    checked={data.isAuthorizedAll}
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
                value={data.scheduleName}
                onChange={e => data.setScheduleName(e.target.value)}
                placeholder={CREATE.PLACEHOLDERS.TITLE}
              />
              <Spacing dir="v" size="2" />
              <Flex justifyContents="start" width="fill">
                <Span>시작</Span>
                <Spacing dir="h" size="0.5" />
                <Input
                  id="start_year"
                  type="number"
                  name="start_year"
                  value={data.startYear}
                  onChange={e => data.setStartYear(Number(e.target.value))}
                  disabled={data.isDateSelected ? true : false}
                  placeholder={CREATE.PLACEHOLDERS.TIME.DATE_START.YY}
                />
                <Spacing dir="h" size="0.25" />
                <Span>/</Span>
                <Spacing dir="h" size="0.25" />
                <Input
                  id="start_month"
                  type="number"
                  name="start_month"
                  value={data.startMonth}
                  onChange={e => data.setStartMonth(Number(e.target.value))}
                  disabled={data.isDateSelected ? true : false}
                  placeholder={CREATE.PLACEHOLDERS.TIME.DATE_START.MM}
                />
                <Spacing dir="h" size="0.25" />
                <Span>/</Span>
                <Spacing dir="h" size="0.25" />
                <Input
                  id="start_date"
                  type="number"
                  name="start_date"
                  value={data.startDate}
                  onChange={e => data.setStartDate(Number(e.target.value))}
                  disabled={data.isDateSelected ? true : false}
                  placeholder={CREATE.PLACEHOLDERS.TIME.DATE_START.DD}
                />
                <Spacing dir="h" size="0.5" />
                <Input
                  id="start_time"
                  type="number"
                  name="start_time"
                  value={data.startTime}
                  onChange={e => data.setStartTime(Number(e.target.value))}
                  disabled={data.isAllday || data.isDateSelected ? true : false}
                  placeholder={CREATE.PLACEHOLDERS.TIME.DATE_START.TT}
                />
                <Spacing dir="h" size="0.25" />
                <Span>:</Span>
                <Spacing dir="h" size="0.25" />
                <Input
                  id="start_minute"
                  type="number"
                  name="start_minute"
                  value={data.startMinute}
                  onChange={e => data.setEndMinute(Number(e.target.value))}
                  disabled={data.isAllday || data.isDateSelected ? true : false}
                  placeholder={CREATE.PLACEHOLDERS.TIME.DATE_START.MM}
                />
              </Flex>
              <Spacing dir="v" size="1" />
              {/* 종료 날짜 및 시간 */}
              <Flex justifyContents="start" width="fill">
                <Span>종료</Span>
                <Spacing dir="h" size="0.5" />
                <Input
                  id="end_year"
                  type="number"
                  name="end_year"
                  value={data.endYear}
                  onChange={e => data.setEndYear(Number(e.target.value))}
                  disabled={data.isAllday || data.isDateSelected ? true : false}
                  placeholder={CREATE.PLACEHOLDERS.TIME.DATE_END.YY}
                />
                <Spacing dir="h" size="0.25" />
                <Span>/</Span>
                <Spacing dir="h" size="0.25" />
                <Input
                  id="end_month"
                  type="number"
                  name="end_month"
                  value={data.endMonth}
                  onChange={e => data.setEndMonth(Number(e.target.value))}
                  disabled={data.isAllday || data.isDateSelected ? true : false}
                  placeholder={CREATE.PLACEHOLDERS.TIME.DATE_END.MM}
                />
                <Spacing dir="h" size="0.25" />
                <Span>/</Span>
                <Spacing dir="h" size="0.25" />
                <Input
                  id="end_date"
                  type="number"
                  name="end_date"
                  value={data.endDate}
                  onChange={e => data.setEndDate(Number(e.target.value))}
                  disabled={data.isAllday || data.isDateSelected ? true : false}
                  placeholder={CREATE.PLACEHOLDERS.TIME.DATE_END.DD}
                />
                <Spacing dir="h" size="0.5" />
                <Input
                  id="end_time"
                  type="number"
                  name="end_time"
                  value={data.endTime}
                  onChange={e => data.setEndTime(Number(e.target.value))}
                  disabled={data.isAllday || data.isDateSelected ? true : false}
                  placeholder={CREATE.PLACEHOLDERS.TIME.DATE_END.TT}
                />
                <Spacing dir="h" size="0.25" />
                <Span>:</Span>
                <Spacing dir="h" size="0.25" />
                <Input
                  id="end_minute"
                  type="number"
                  name="end_minute"
                  value={data.endMinute}
                  onChange={e => data.setEndMinute(Number(e.target.value))}
                  disabled={data.isAllday || data.isDateSelected ? true : false}
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
                      data.setIsAllday(!data.isAllday);
                      if (data.isDateSelected) data.setIsDateSelected(false);
                    }}
                    checked={data.isAllday}
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
                      data.setIsDateSelected(!data.isDateSelected);
                      if (data.isAllday) data.setIsAllday(false);
                    }}
                    checked={data.isDateSelected}
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
                  {data.participants.map(p => (
                    <Chip
                      key={p.userId}
                      label={`${p.userName}@${p.userId}`}
                      type="keyword"
                      onDelete={() => data.onDelete(p.userSeq)}
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
                value={data.partySearchInput}
                onChange={e => data.partyAutoComplete(e.target.value)}
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
                  {data.partySearchResult.map(r => {
                    if (r.groupName === null)
                      return (
                        <Flex flexDirection="row" justifyContents="start">
                          {r.groupMember.map(m => (
                            <div key={m.userId}>
                              <button
                                className={AC}
                                onClick={() =>
                                  data.setParticipants([
                                    ...data.participants,
                                    m,
                                  ])
                                }
                              >
                                <Chip
                                  label={`${m.userName}@${m.userId}`}
                                  type="suggestion"
                                />
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
                value={data.scheduleContent}
                onChange={e => data.setScheduleContent(e.target.value)}
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
                    type={
                      data.alarmTime === option ? 'filterselected' : 'filter'
                    }
                    onFiltered={() => data.setAlarmTime(option)}
                    shape="square"
                    label={CREATE.ALARMS[option]}
                  />
                ))}
              </Flex>
              <Spacing dir="v" size="2" />
              <Span>{CREATE.PLACEHOLDERS.CATEGORY}</Span>
              <Spacing dir="v" size="1" />
              <Container>
                {data.category.map(c => (
                  <Category
                    color={c.categoryColor}
                    categoryName={c.categoryName}
                    key={c.categorySeq}
                    onClick={() => data.setCategorySeq(c.categorySeq)}
                  />
                ))}
              </Container>
              <Spacing dir="v" size="0.5" />
              <div>
                <Input
                  type="text"
                  id="categoryadd"
                  onChange={e => data.setCategoryInput(e.target.value)}
                  onKeyDown={e => {
                    e.key === 'enter' && data.addCategory(data.categoryInput);
                    data.setCategoryInput('');
                  }}
                  value={data.categoryInput}
                  name="categoryadd"
                  placeholder="카테고리 추가"
                />
              </div>
              <Spacing dir="v" size="2" />
              <Textarea
                id="memo"
                height="10rem"
                value={data.scheduleMemo}
                onChange={e => data.setScheduleMemo(e.target.value)}
                placeholder={CREATE.PLACEHOLDERS.MEMO}
              />
              <Spacing dir="v" size="3" />
              <Btn type="submit" onClick={data.submit} variant="key">
                일정 추가
              </Btn>
            </Flex>
          </Form>
          <Spacing dir="v" size="5" />
        </div>
      )}
    </ScheduleEditData>
  );
}
