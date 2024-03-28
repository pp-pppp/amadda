import FRIENDS from '@/constants/friend/friend-ui';
import { ROUTES } from '@/store/friend/friend-router/page-slice';
import { useFriendRouter } from '@/store/friend/friend-router/use-friend-router';
import { Btn } from '@amadda/external-temporal';

interface FriendControlButtonProps {
  PushToFriend: (to: (typeof ROUTES)[number]) => void;
}

export function FriendNavigation() {
  const [PATH, PushToFriend] = useFriendRouter(state => [state.PATH, state.PushToFriend]);

  switch (PATH) {
    case 'READ':
      return <FriendNavigation.PathIsRead PushToFriend={PushToFriend} />;
    case 'SEARCH':
      return <FriendNavigation.PathIsSearch PushToFriend={PushToFriend} />;
    default:
      return <></>;
  }
}
FriendNavigation.PathIsRead = ({ PushToFriend }: FriendControlButtonProps) => {
  return (
    <Btn type="button" variant="white" onClick={() => PushToFriend('ADD')}>
      {FRIENDS.BTN.ADD_GROUP}
    </Btn>
  );
};

FriendNavigation.PathIsSearch = ({ PushToFriend }: FriendControlButtonProps) => {
  return (
    <Btn type="button" variant="white" onClick={() => PushToFriend('READ')}>
      {FRIENDS.BTN.READ}
    </Btn>
  );
};
