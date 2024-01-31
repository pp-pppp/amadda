import { H2 } from '@amadda/external-temporal';
import { FriendSearchInput } from './FriendSearchInput/FriendSearchInput';

export function FriendFrameHeader() {
  return (
    <>
      <H2>친구</H2>
      <FriendSearchInput />
    </>
  );
}
