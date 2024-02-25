import { Spacing } from '@amadda/external-temporal';
import { FriendGroupHeader } from '../FriendGroupHeader/FriendGroupHeader';
import { FriendGroupBody } from '../FriendGroupBody/FriendGroupBody';
import { useFriendRouter } from '@U/store/friendRouter/useFriendRouter';

export interface FriendGroupProps {
  groupSeq?: number;
  groupName?: string;
  groupMember: { userSeq: number; userName: string; userId: string; imageUrl: string }[];
}
export function FriendGroup({ groupSeq, groupName, groupMember }: FriendGroupProps) {
  return (
    <>
      <FriendGroupHeader groupName={groupName} groupSeq={groupSeq} groupMember={groupMember} />
      <Spacing size="0.5" />
      <FriendGroupBody groupMember={groupMember} />
    </>
  );
}
