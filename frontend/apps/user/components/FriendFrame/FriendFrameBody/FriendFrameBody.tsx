import { useFriendRouter } from '@U/store/friendRouter/useFriendRouter';
import { useFriend, useSearchFriend } from '@U/hooks/useFriend';
import { FriendLoading } from '../Fallback/FriendLoading';
import { NoFriend } from '../Fallback/NoFriend';
import { FriendGroupEdit } from '../../FriendGroup/FriendGroupEdit';
import { FriendGroup } from '../../FriendGroup/FriendGroup';
import { useFriendSearchStore } from '@U/store/friendSearchForm/useFriendSearchStore';

export function FriendFrameBody() {
  const [PATH, PushToFriend] = useFriendRouter(state => [state.PATH, state.PushToFriend]);

  switch (PATH) {
    case 'ADD':
      return <FriendFrameBody.FriendAddBlock />;
    case 'READ':
      return <FriendFrameBody.FriendReadResultBlock />;
    case 'SEARCH':
      return <FriendFrameBody.FriendSearchResultBlock />;
    case 'EDIT':
      return <FriendFrameBody.FriendEditBlock />;
    default:
      PushToFriend('READ');
  }
}

FriendFrameBody.FriendAddBlock = () => {
  const { data, error, isLoading } = useFriend();

  if (isLoading) return <FriendLoading />;
  if (data?.length === 0) return <NoFriend />;
  else return <>{data?.filter(group => <FriendGroupEdit {...group} />)}</>;
};

FriendFrameBody.FriendReadResultBlock = () => {
  const { data, error, isLoading } = useFriend();

  if (isLoading) return <FriendLoading />;
  if (data?.length === 0) return <NoFriend />;
  else return <>{data?.map(group => <FriendGroup {...group} />)}</>;
};

FriendFrameBody.FriendSearchResultBlock = () => {
  const [searchKey] = useFriendSearchStore(state => [state.searchKey]);
  const { data, error, isLoading } = useSearchFriend({ friendSearch: searchKey });

  if (isLoading) return <FriendLoading />;
  if (data?.length === 0) return <NoFriend />;
  else return <>{data?.map(group => <FriendGroup {...group} />)}</>;
};

FriendFrameBody.FriendEditBlock = () => {
  const { data, error, isLoading } = useFriend();

  if (isLoading) return <FriendLoading />;
  if (data?.length === 0) return <NoFriend />;
  else return <>{data?.filter(group => <FriendGroupEdit {...group} />)}</>;
};
