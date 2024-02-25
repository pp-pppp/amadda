import { useSearchFriend } from '@U/hooks/useFriend';
import { useFriendRouter } from '@U/store/friendRouter/useFriendRouter';
import { useFriendSearchStore } from '@U/store/friendSearchForm/useFriendSearchStore';
import { NoFriend } from '../Fallback/NoFriend';
import { FriendLoading } from '../Fallback/FriendLoading';
import { FriendGroup } from './FriendGroup/FriendGroup';

export function FriendSearchResultBlock() {
  const [PATH, PushToFriend] = useFriendRouter(state => [state.PATH, state.PushToFriend]);
  const [searchKey] = useFriendSearchStore(state => [state.searchKey]);
  const { data, error, isLoading } = useSearchFriend({ friendSearch: searchKey });

  if (isLoading) return <FriendLoading />;
  if (data?.length === 0) return <NoFriend />;
  else return <>{data?.map(group => <FriendGroup {...group} />)}</>;
}
