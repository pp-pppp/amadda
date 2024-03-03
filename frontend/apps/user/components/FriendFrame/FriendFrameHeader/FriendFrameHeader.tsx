import { Flex, H2 } from '@amadda/external-temporal';
import { FriendSearchInput } from './FriendSearchInput/FriendSearchInput';
import { FriendRouteControl } from '../FriendRouteControl/FriendRouteControl';
import FRIENDS from '@U/constants/FRIENDS';

export function FriendFrameHeader() {
  return (
    <Flex flexDirection="column" justifyContents="flexStart" alignItems="center">
      <H2>{FRIENDS.TITLE}</H2>
      <FriendRouteControl />
      <FriendSearchInput />
    </Flex>
  );
}
