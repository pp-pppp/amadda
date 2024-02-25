import { useFriendRouter } from '@U/store/friendRouter/useFriendRouter';
import { FriendReadResult } from './FriendReadResultBlock';
import { FriendSearchResult } from './FriendSearchResultBlock';
import { FriendEditBlock } from './FriendEditBlock';
import { FriendAddBlock } from './FriendAddBlock';

export function FriendFrameBody() {
  const [PATH, PushToFriend] = useFriendRouter(state => [state.PATH, state.PushToFriend]);

  switch (PATH) {
    case 'ADD':
      return <FriendAddBlock />;
    case 'READ':
      return <FriendReadResult />;
    case 'SEARCH':
      return <FriendSearchResult />;
    case 'EDIT':
      return <FriendEditBlock />;
    default:
      PushToFriend('READ');
  }
}
