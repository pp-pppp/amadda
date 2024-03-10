import { BtnRound, Chip, Form, Input, Spacing } from '@amadda/external-temporal';
import { createGroup, useFriend } from '@U/hooks/use-friend';
import { useGroupRequestFormStore } from '@U/store/group-request-form/use-group-request-store';
import { useForm } from '@amadda/react-util-hooks';
import { useShallow } from 'zustand/react/shallow';
import { GroupRequestForm } from '@U/store/group-request-form/group-request-form-slice';
import { Friend } from '@U/components/Friend/Friend';
import { useFriendRouter } from '@U/store/friend-router/use-friend-router';
import FRIENDS from '@U/constants/FRIENDS';
import { friendGroupValidator } from './friend-group-validator';
import { GroupBody } from './friend-group-layout/friend-group-body';
import { GroupHeader } from './friend-group-layout/friend-group-header';

export function FriendGroupCreate() {
  const pushToFriend = useFriendRouter(state => state.PushToFriend);

  const [values, setGroup, addUser, deleteUser, setFormData] = useGroupRequestFormStore(
    useShallow(state => [state.values, state.setGroup, state.addUser, state.deleteUser, state.setFormData])
  );

  const { data, mutate } = useFriend();
  const [ALL_FRIENDS] = data?.filter(group => group.groupName === null) || [];

  const { submit, handleChange } = useForm<GroupRequestForm>({
    initialValues: values,
    onSubmit: () => createGroup(values),
    setExternalStoreData: setFormData,
    setExternalStoreValues: setGroup,
    validator: friendGroupValidator,
  });

  return (
    <>
      <Form
        formName="FriendGroupCreate"
        onSubmit={async e => {
          e.preventDefault();
          submit(values);
          await mutate();
          pushToFriend('READ');
        }}
      >
        <GroupHeader>
          <Input id="groupName" placeholder={FRIENDS.INPUT.ADD_GROUP} type="text" name="groupName" onChange={handleChange} value={values.groupName} />
          <BtnRound type="submit" variant="key">
            {FRIENDS.BTN.ADD_GROUP}
          </BtnRound>
          <BtnRound type="button" variant="white" onClick={() => pushToFriend('READ')}>
            {FRIENDS.BTN.CANCEL}
          </BtnRound>
        </GroupHeader>
        <Spacing size="0.5" />
        {values.groupMembers.map(selected => (
          <Chip type="keyword" shape="square" onDelete={() => deleteUser(selected)}>
            {selected.userName}
          </Chip>
        ))}
        <Spacing size="0.5" />
        <GroupBody>
          {ALL_FRIENDS.groupMember.map((friend, _, friends) => (
            <Friend groupMember={friend} onSelect={addUser} selected={Boolean(friends.find(f => f.userSeq === friend.userSeq))} />
          ))}
        </GroupBody>
      </Form>
    </>
  );
}
