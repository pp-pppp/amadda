import FRIENDS from '@U/constants/FRIENDS';
import { ROUTES } from '@U/store/friendRouter/pageSlice';
import { useFriendRouter } from '@U/store/friendRouter/useFriendRouter';
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
