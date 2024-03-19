import { Btn, BtnRound, Chip, Form, Input, Spacing } from '@amadda/external-temporal';
import { updateGroup, useFriend } from '@U/hooks/use-friend';
import { useGroupRequestFormStore } from '@U/store/group-request-form/use-group-request-store';
import { useEffect } from 'react';
import { useForm } from '@amadda/react-util-hooks';
import { useShallow } from 'zustand/react/shallow';
import { GroupRequestForm } from '@U/store/group-request-form/group-request-form-slice';
import { Friend } from '@U/components/Friend/Friend';
import { useFriendRouter } from '@U/store/friend-router/use-friend-router';
import FRIENDS from '@U/constants/FRIENDS';
import { Group } from '@amadda/global-types';
import { GroupBody } from './friend-group-layout/friend-group-body';
import { GroupHeader } from './friend-group-layout/friend-group-header';

export function FriendGroupUpdate() {
  const [pushToFriend, updatingGroupSeq, setUpdatingGroupSeq] = useFriendRouter(state => [
    state.PushToFriend,
    state.updatingGroupSeq,
    state.setUpdatingGroupSeq,
  ]);

  const [values, setGroup, addUser, deleteUser, setFormData] = useGroupRequestFormStore(
    useShallow(state => [state.values, state.setGroup, state.addUser, state.deleteUser, state.setFormData])
  );

  const { data, mutate } = useFriend();

  const { ALL_FRIENDS, UPDATING_GROUP } = data?.reduce(
    (acc, group) => {
      if (group.groupSeq === updatingGroupSeq) acc.UPDATING_GROUP = group;
      if (group.groupName === null) acc.ALL_FRIENDS = group;
      return acc;
    },
    { ALL_FRIENDS: null as Group | null, UPDATING_GROUP: null as Group | null }
  ) ?? { ALL_FRIENDS: null, UPDATING_GROUP: null };

  const { submit, handleChange } = useForm<GroupRequestForm>({
    initialValues: values,
    onSubmit: () => updateGroup(updatingGroupSeq, values),
    setExternalStoreData: setFormData,
    setExternalStoreValues: setGroup,
  });

  useEffect(() => {
    if (UPDATING_GROUP) {
      const group: GroupRequestForm = { groupName: UPDATING_GROUP.groupName, groupMembers: UPDATING_GROUP.groupMember };
      setGroup(group);
    }
  }, [updatingGroupSeq]);

  return (
    <>
      <Form
        formName="FriendGroupEdit"
        onSubmit={async e => {
          e.preventDefault();
          submit(values);
          await mutate();
          setUpdatingGroupSeq(null);
          pushToFriend('READ');
        }}
      >
        <GroupHeader>
          <Input id="groupName" type="text" name="groupName" onChange={handleChange} value={values.groupName} />
          <BtnRound type="submit" variant="key">
            {FRIENDS.BTN.GROUP_SAVE}
          </BtnRound>
          <BtnRound
            type="button"
            variant="white"
            onClick={() => {
              pushToFriend('READ');
              setUpdatingGroupSeq(null);
            }}
          >
            {FRIENDS.BTN.CANCEL}
          </BtnRound>
        </GroupHeader>
        <Spacing size="0.5" />
        <div>
          {values.groupMembers.map(selected => (
            <Chip type="keyword" shape="square" onDelete={() => deleteUser(selected)}>
              {selected.userName}
            </Chip>
          ))}
        </div>
        <Spacing size="0.5" />
        <GroupBody>
          {ALL_FRIENDS?.groupMember.map((friend, _, friends) => (
            <Friend groupMember={friend} onSelect={addUser} selected={Boolean(friends.find(f => f.userSeq === friend.userSeq))} />
          ))}
        </GroupBody>
        <Btn
          type="button"
          variant="black"
          onClick={() => {
            setUpdatingGroupSeq(null);
          }}
        >
          {FRIENDS.BTN.GROUP_DELETE}
        </Btn>
      </Form>
    </>
  );
}
