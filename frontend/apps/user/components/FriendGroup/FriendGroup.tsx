import { Flex, H3, Input, List, Spacing } from '@amadda/external-temporal';
import { useFriendRouter } from '@U/store/friendRouter/useFriendRouter';
import { Friend } from '../Friend/Friend';
import { useGroupRequestStore } from '@U/store/friendGroupForm/useGroupRequestStore';
import FRIENDS from '@U/constants/FRIENDS';
import { FriendGroupRouteControl } from './FriendGroupRouteControl/FriendGroupRouteControl';

export interface FriendGroupProps {
  groupSeq?: number;
  groupName?: string;
  groupMember: { userSeq: number; userName: string; userId: string; imageUrl: string }[];
}
export function FriendGroup({ groupSeq, groupName, groupMember }: FriendGroupProps) {
  return (
    <>
      <FriendGroup.Header groupName={groupName} groupSeq={groupSeq} groupMember={groupMember} />
      <Spacing size="0.5" />
      <FriendGroup.Body groupMember={groupMember} />
    </>
  );
}

FriendGroup.Header = ({ groupName, groupSeq, groupMember }: FriendGroupProps) => {
  const [PATH] = useFriendRouter(state => [state.PATH]);
  const setGroupName = useGroupRequestStore(state => state.setGroupName);

  return (
    <Flex flexDirection="row" justifyContents="flexStart" alignItems="center">
      {(PATH === 'READ' || PATH === 'SEARCH') && <H3>{groupName || FRIENDS.GROUPS.ALL_FRIENDS}</H3>}
      {(PATH === 'EDIT' || PATH === 'ADD') && (
        <Input id="groupName" type="text" name="groupName" onChange={e => setGroupName(e.target.value)} value={groupName} />
      )}
      <FriendGroupRouteControl groupName={groupName} groupSeq={groupSeq} groupMember={groupMember} />
    </Flex>
  );
};

interface FriendGroupBodyProps {
  groupMember: { userSeq: number; userName: string; userId: string; imageUrl: string }[];
}
FriendGroup.Body = ({ groupMember }: FriendGroupBodyProps) => {
  const [PATH] = useFriendRouter(state => [state.PATH]);
  return (
    <List.Ul>
      {groupMember.map(member => (
        <List.Li key={member.userSeq}>
          <Friend groupMember={member} status={PATH} />
        </List.Li>
      ))}
    </List.Ul>
  );
};
