import FRIENDS from '@U/constants/FRIENDS';
import { H3 } from '@amadda/external-temporal';

interface FriendGroupHeaderProps {
  groupName?: string;
}
export function FriendGroupHeader({ groupName = FRIENDS.GROUPS.ALL_FRIENDS }: FriendGroupHeaderProps) {
  return <H3>{groupName}</H3>;
}
