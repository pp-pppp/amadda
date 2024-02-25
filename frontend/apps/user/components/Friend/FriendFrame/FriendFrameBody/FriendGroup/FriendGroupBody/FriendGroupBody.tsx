import { List } from '@amadda/external-temporal';
import { Friend } from './Friend/Friend';
import { useFriendRouter } from '@U/store/friendRouter/useFriendRouter';

interface FriendGroupBodyProps {
  groupMember: { userSeq: number; userName: string; userId: string; imageUrl: string }[];
}
export function FriendGroupBody({ groupMember }: FriendGroupBodyProps) {
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
}
