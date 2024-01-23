import { Flex, Chip, Input, Spacing } from '@amadda/external-temporal';
import { AC, PARTICIPANTS, SEARCHRESULT } from '../ScheduleEdit.css';
import { useScheduleEditStore } from '@SCH/store/schedule-create/useScheduleEditStore';
import { CREATE } from '@SCH/constants/CREATE';

export function Participants() {
  const [values, setValues] = useScheduleEditStore(state => [state.values, state.setValues]);

  return (
    <>
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
    </>
  );
}
