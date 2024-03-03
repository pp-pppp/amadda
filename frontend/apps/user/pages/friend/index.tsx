import { FriendFrame } from '@U/components/FriendFrame/FriendFrame';
import { ErrorBoundary } from '@amadda/external-temporal';
import { FriendFrameHeader } from '@U/components/FriendFrame/FriendFrameHeader/FriendFrameHeader';
import { FriendRouter } from '@U/components/FriendFrame/FriendRouter/FriendRouter';

export default function FriendPage() {
  return (
    <FriendFrame>
      <ErrorBoundary>
        <FriendFrameHeader />
        <FriendRouter />
      </ErrorBoundary>
    </FriendFrame>
  );
}
