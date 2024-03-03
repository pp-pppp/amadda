import { useFriendRouter } from '@U/store/friendRouter/useFriendRouter';
import { useFriend, useSearchFriend } from '@U/hooks/useFriend';
import { FriendLoading } from '../../FriendFallback/FriendLoading';
import { NoFriend } from '../../FriendFallback/NoFriend';
import { FriendGroupEdit } from '../../FriendGroup/FriendGroupEdit/FriendGroupEdit';
import { FriendGroup } from '../../FriendGroup/FriendGroup';
import { useFriendSearchStore } from '@U/store/friendSearchForm/useFriendSearchStore';

export function FriendRouter() {
  const [PATH, PushToFriend] = useFriendRouter(state => [state.PATH, state.PushToFriend]);

  switch (PATH) {
    case 'ADD':
      return <FriendRouter.FriendAddBlock />;
    case 'READ':
      return <FriendRouter.FriendReadResultBlock />;
    case 'SEARCH':
      return <FriendRouter.FriendSearchResultBlock />;
    case 'EDIT':
      return <FriendRouter.FriendEditBlock />;
    default:
      PushToFriend('READ');
  }
}

FriendRouter.FriendAddBlock = () => {
  const { data, error, isLoading } = useFriend();

  if (isLoading) return <FriendLoading />;
  if (data?.length === 0) return <NoFriend />;
  else return <>{data?.filter(group => <FriendGroupEdit {...group} />)}</>;
};

FriendRouter.FriendReadResultBlock = () => {
  const { data, error, isLoading } = useFriend();

  if (isLoading) return <FriendLoading />;
  if (data?.length === 0) return <NoFriend />;
  else return <>{data?.map(group => <FriendGroup {...group} />)}</>;
};

FriendRouter.FriendSearchResultBlock = () => {
  const [searchKey] = useFriendSearchStore(state => [state.searchKey]);
  const { data, error, isLoading } = useSearchFriend({ friendSearch: searchKey });

  if (isLoading) return <FriendLoading />;
  if (data?.length === 0) return <NoFriend />;
  else return <>{data?.map(group => <FriendGroup {...group} />)}</>;
};

FriendRouter.FriendEditBlock = () => {
  const { data, error, isLoading } = useFriend();

  if (isLoading) return <FriendLoading />;
  if (data?.length === 0) return <NoFriend />;
  else return <>{data?.filter(group => <FriendGroupEdit {...group} />)}</>;
};
