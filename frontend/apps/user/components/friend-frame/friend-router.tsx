import { useFriendRouter } from '@U/store/friend-router/use-friend-router';
import { FriendGroupEdit } from '../friend-group/friend-group-edit';
import { FriendGroupCreate } from '@U/components/friend-group/friend-group-create';
import { FriendGroupRead } from '@U/components/friend-group/friend-group-read';
import { FriendGroupSearch } from '@U/components/friend-group/friend-group-search';

export function FriendRouter() {
  const [PATH, PushToFriend] = useFriendRouter(state => [state.PATH, state.PushToFriend]);

  switch (PATH) {
    case 'ADD':
      return <FriendGroupCreate />;
    case 'READ':
      return <FriendGroupRead />;
    case 'SEARCH':
      return <FriendGroupSearch />;
    case 'EDIT':
      return <FriendGroupEdit />;
    default:
      PushToFriend('READ');
      return <FriendGroupRead />;
  }
}
