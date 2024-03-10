import { Flex, H2 } from '@amadda/external-temporal';
import { FriendSearchInput } from '../../friend-search-input/friend-search-input';
import { FriendNavigation } from '../friend-navigation';
import FRIENDS from '@U/constants/FRIENDS';

export function FriendFrameHeader() {
  return (
    <Flex flexDirection="column" justifyContents="flexStart" alignItems="center">
      <H2>{FRIENDS.TITLE}</H2>
      <FriendNavigation />
      <FriendSearchInput />
    </Flex>
  );
}
