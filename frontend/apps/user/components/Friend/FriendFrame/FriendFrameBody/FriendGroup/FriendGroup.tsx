import { Spacing } from '@amadda/external-temporal';
import { FriendGroupHeader } from './FriendGroupHeader/FriendGroupHeader';
import { FriendGroupBody } from './FriendGroupBody/FriendGroupBody';

export interface FriendGroupProps {
  groupSeq?: number;
  groupName?: string;
  groupMember: { userSeq: number; userName: string; userId: string; imageUrl: string }[];
}
export function FriendGroup({ groupSeq, groupName, groupMember }: FriendGroupProps) {
  return (
    <>
      <FriendGroupHeader groupName={groupName} />
      <Spacing size="0.5" />
      <FriendGroupBody groupMember={groupMember} />
    </>
  );
}
