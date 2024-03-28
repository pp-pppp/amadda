import { BtnRound, Chip, Form, Input, Spacing } from '@amadda/external-temporal';
import { createGroup, useFriend } from '@/hooks/friend/use-friend';
import { useGroupRequestFormStore } from '@/store/friend/group-request-form/use-group-request-store';
import { useForm } from '@amadda/react-util-hooks';
import { useShallow } from 'zustand/react/shallow';
import { GroupRequestForm } from '@/store/friend/group-request-form/group-request-form-slice';
import { Friend } from '@/components/friend-component/friend';
import { useFriendRouter } from '@/store/friend/friend-router/use-friend-router';
import FRIENDS from '@/constants/friend/friend-ui';
import { friendGroupValidator } from '../../constants/friend/friend-group-validator';
import { GroupBody } from './friend-group-layout/friend-group-body';
import { GroupHeader } from './friend-group-layout/friend-group-header';

export function FriendGroupCreate() {
  const pushToFriend = useFriendRouter(state => state.PushToFriend);

  const [values, setGroup, submit, handleChange, addUser, deleteUser, setFormData] = useGroupRequestFormStore(
    useShallow(state => [state.values, state.setGroup, state.submit, state.handleChange, state.addUser, state.deleteUser, state.setFormData])
  );

  const { data, mutate } = useFriend();
  const [ALL_FRIENDS] = data?.filter(group => group.groupName === null) || [];

  useForm<GroupRequestForm>({
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
          <Chip label={selected.userName} type="keyword" shape="square" onDelete={() => deleteUser(selected)} />
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
