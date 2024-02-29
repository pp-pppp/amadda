import FRIENDS from '@U/constants/FRIENDS';
import { Flex, H3, Input } from '@amadda/external-temporal';
import { FriendGroupProps } from '../FriendGroup';
import { FriendGroupEditRouteControl } from './FriendGroupEditRouteControl/FriendGroupEditRouteControl';
import { useFriendRouter } from '@U/store/friendRouter/useFriendRouter';
import { useGroupRequestStore } from '@U/store/friendGroupForm/useGroupRequestStore';

export function FriendGroupHeader({ groupName, groupSeq, groupMember }: FriendGroupProps) {
  const [PATH] = useFriendRouter(state => [state.PATH]);
  const setGroupName = useGroupRequestStore(state => state.setGroupName);

  return (
    <Flex flexDirection="row" justifyContents="flexStart" alignItems="center">
      {(PATH === 'READ' || PATH === 'SEARCH') && <H3>{groupName || FRIENDS.GROUPS.ALL_FRIENDS}</H3>}
      {(PATH === 'EDIT' || PATH === 'ADD') && (
        <Input id="groupName" type="text" name="groupName" onChange={e => setGroupName(e.target.value)} value={groupName} />
      )}
      <FriendGroupEditRouteControl groupName={groupName} groupSeq={groupSeq} groupMember={groupMember} />
    </Flex>
  );
}
