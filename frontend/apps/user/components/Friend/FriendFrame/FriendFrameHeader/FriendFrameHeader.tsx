import { Flex, H2 } from '@amadda/external-temporal';
import { FriendSearchInput } from './FriendSearchInput/FriendSearchInput';
import { FriendRouteControl } from './FriendRouteControl/FriendRouteControl';

export function FriendFrameHeader() {
  return (
    <Flex flexDirection="column" justifyContents="flexStart" alignItems="center">
      <H2>친구</H2>
      <FriendRouteControl />
      <FriendSearchInput />
    </Flex>
  );
}
