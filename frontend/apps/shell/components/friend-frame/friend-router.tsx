import { useFriendRouter } from '@/store/friend/friend-router/use-friend-router';
import { FriendGroupUpdate } from '@/components/friend-group/friend-group-update';
import { FriendGroupCreate } from '@/components/friend-group/friend-group-create';
import { FriendGroupRead } from '@/components/friend-group/friend-group-read';
import { FriendGroupSearch } from '@/components/friend-group/friend-group-search';

export function FriendRouter() {
  const [PATH, PushToFriend] = useFriendRouter(state => [state.PATH, state.PushToFriend]);

  switch (PATH) {
    case 'ADD':
      return <FriendGroupCreate />;
    case 'READ':
      return <FriendGroupRead />;
    case 'SEARCH':
      return <FriendGroupSearch />;
    case 'UPDATE':
      return <FriendGroupUpdate />;
    default:
      PushToFriend('READ');
      return <FriendGroupRead />;
  }
}
