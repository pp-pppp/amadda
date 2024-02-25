import { useFriendRouter } from '@U/store/friendRouter/useFriendRouter';
import { FriendReadResultBlock } from './FriendReadResultBlock';
import { FriendSearchResultBlock } from './FriendSearchResultBlock';
import { FriendEditBlock } from './FriendEditBlock';
import { FriendAddBlock } from './FriendAddBlock';

export function FriendFrameBody() {
  const [PATH, PushToFriend] = useFriendRouter(state => [state.PATH, state.PushToFriend]);

  switch (PATH) {
    case 'ADD':
      return <FriendAddBlock />;
    case 'READ':
      return <FriendReadResultBlock />;
    case 'SEARCH':
      return <FriendSearchResultBlock />;
    case 'EDIT':
      return <FriendEditBlock />;
    default:
      PushToFriend('READ');
  }
}
