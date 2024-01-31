import { List } from '@amadda/external-temporal';
import { Friend } from './Friend/Friend';

interface FriendGroupBodyProps {
  groupMember: { userSeq: number; userName: string; userId: string; imageUrl: string }[];
}
export function FriendGroupBody({ groupMember }: FriendGroupBodyProps) {
  return (
    <List.Ul>
      {groupMember.map(member => (
        <List.Li key={member.userSeq}>
          <Friend groupMember={member} />
        </List.Li>
      ))}
    </List.Ul>
  );
}
