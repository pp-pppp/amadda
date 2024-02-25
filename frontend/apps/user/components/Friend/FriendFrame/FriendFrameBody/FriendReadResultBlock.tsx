import { useFriend } from '@U/hooks/useFriend';
import { useFriendRouter } from '@U/store/friendRouter/useFriendRouter';
import { FriendLoading } from '../Fallback/FriendLoading';
import { NoFriend } from '../Fallback/NoFriend';
import { FriendGroup } from './FriendGroup/FriendGroup';

export function FriendReadResultBlock() {
  const [PATH, PushToFriend] = useFriendRouter(state => [state.PATH, state.PushToFriend]);
  const { data, error, isLoading } = useFriend();

  if (isLoading) return <FriendLoading />;
  if (data?.length === 0) return <NoFriend />;
  else return <>{data?.map(group => <FriendGroup {...group} />)}</>;
}
