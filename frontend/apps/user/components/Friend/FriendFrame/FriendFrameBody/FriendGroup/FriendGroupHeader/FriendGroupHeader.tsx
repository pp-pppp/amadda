import FRIENDS from '@U/constants/FRIENDS';
import { Flex, H3 } from '@amadda/external-temporal';
import { FriendGroupProps } from '../FriendGroup';
import { FriendGroupEditRouteControl } from './FriendGroupEditRouteControl/FriendGroupEditRouteControl';

export function FriendGroupHeader({ groupName, groupSeq, groupMember }: FriendGroupProps) {
  return (
    <Flex flexDirection="row" justifyContents="flexStart" alignItems="center">
      <H3>{groupName || FRIENDS.GROUPS.ALL_FRIENDS}</H3>
      <FriendGroupEditRouteControl groupName={groupName} groupSeq={groupSeq} groupMember={groupMember} />
    </Flex>
  );
}
