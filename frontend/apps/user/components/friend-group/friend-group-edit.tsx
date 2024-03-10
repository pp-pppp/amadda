import { Btn, BtnRound, Chip, Form, Input, Spacing } from '@amadda/external-temporal';
import { updateGroup, useFriend } from '@U/hooks/useFriend';
import { useGroupRequestFormStore } from '@U/store/friendGroupForm/useGroupRequestStore';
import { useEffect } from 'react';
import { useForm } from '@amadda/react-util-hooks';
import { useShallow } from 'zustand/react/shallow';
import { GroupRequestForm } from '@U/store/friendGroupForm/groupRequestFormSlice';
import { Friend } from '@U/components/Friend/Friend';
import { useFriendRouter } from '@U/store/friendRouter/useFriendRouter';
import FRIENDS from '@U/constants/FRIENDS';
import { Group } from '@amadda/global-types';
import { GroupBody } from './friend-group-layout/friend-group-body';
import { GroupHeader } from './friend-group-layout/friend-group-header';

export function FriendGroupEdit() {
  const [pushToFriend, editing, setEditing] = useFriendRouter(state => [state.PushToFriend, state.editing, state.setEditing]);

  const [values, setGroup, addUser, deleteUser, setFormData] = useGroupRequestFormStore(
    useShallow(state => [state.values, state.setGroup, state.addUser, state.deleteUser, state.setFormData])
  );

  const { data, mutate } = useFriend();

  const { ALL_FRIENDS, EDITING_GROUP } = data?.reduce(
    (acc, group) => {
      if (group.groupSeq === editing) acc.EDITING_GROUP = group;
      if (group.groupName === null) acc.ALL_FRIENDS = group;
      return acc;
    },
    { ALL_FRIENDS: null as Group | null, EDITING_GROUP: null as Group | null }
  ) ?? { ALL_FRIENDS: null, EDITING_GROUP: null };

  useEffect(() => {
    if (EDITING_GROUP) {
      const group: GroupRequestForm = { groupName: EDITING_GROUP.groupName, groupMembers: EDITING_GROUP.groupMember };
      setGroup(group);
    }
  }, [editing]);

  const { submit, handleChange } = useForm<GroupRequestForm>({
    initialValues: values,
    onSubmit: () => updateGroup(editing, values),
    setExternalStoreData: setFormData,
    setExternalStoreValues: setGroup,
  });

  return (
    <>
      <Form
        formName="FriendGroupEdit"
        onSubmit={async e => {
          e.preventDefault();
          submit(values);
          await mutate();
          setEditing(null);
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
              setEditing(null);
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
            setEditing(null);
          }}
        >
          {FRIENDS.BTN.GROUP_DELETE}
        </Btn>
      </Form>
    </>
  );
}
