import { Chip, Spacing } from '@amadda/external-temporal';
import { FriendGroupHeader } from '../FriendGroupHeader/FriendGroupHeader';
import { FriendGroupBody } from '../FriendGroupBody/FriendGroupBody';
import { useFriend } from '@U/hooks/useFriend';
import { useGroupRequestStore } from '@U/store/friendGroupForm/useGroupRequestStore';
import { useEffect } from 'react';

export interface FriendGroupEditProps {
  groupSeq?: number;
}
export function FriendGroupEdit({ groupSeq }: FriendGroupEditProps) {
  const [users, groupName, setGroupName, addUser, deleteUser] = useGroupRequestStore(state => [
    state.users,
    state.groupName,
    state.setGroupName,
    state.addUser,
    state.deleteUser,
  ]);

  const { data, error, isLoading } = useFriend();
  const [ALL_FRIENDS] = data?.filter(group => group.groupName === null || group.groupSeq === groupSeq) || [];
  const [TARGET_GROUP] = data?.filter(group => group.groupSeq === groupSeq) || [];
  useEffect(() => {
    addUser(TARGET_GROUP.groupMember);
  }, [groupSeq]);

  return (
    <>
      <FriendGroupHeader groupName={groupName} groupSeq={TARGET_GROUP.groupSeq} groupMember={TARGET_GROUP.groupMember} />
      {/* TODO: 그룹 이름 수정 기능 */}
      <Spacing size="0.5" />
      {users.map(selected => (
        <Chip type="keyword" shape="square" onDelete={e => deleteUser(selected)}>
          {selected.userName}
        </Chip>
      ))}
      <Spacing size="0.5" />
      {/* TODO: 선택시 ADD USER */}
      <FriendGroupBody groupMember={ALL_FRIENDS.groupMember} />
    </>
  );
}
