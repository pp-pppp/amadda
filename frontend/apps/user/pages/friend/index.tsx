import { FriendFrame } from '@U/components/FriendFrame/FriendFrame';
import { ErrorBoundary } from '@amadda/external-temporal';
import { FriendFrameHeader } from '@U/components/FriendFrame/FriendFrameHeader/FriendFrameHeader';
import { FriendFrameBody } from '@U/components/FriendFrame/FriendFrameBody/FriendFrameBody';

export default function FriendPage() {
  return (
    <FriendFrame>
      <ErrorBoundary>
        <FriendFrameHeader />
        <FriendFrameBody />
      </ErrorBoundary>
    </FriendFrame>
  );
}
