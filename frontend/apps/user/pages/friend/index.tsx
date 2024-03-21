import { FriendFrameLayout } from '@U/components/friend-frame/friend-frame-layout/friend-frame-layout';
import { ErrorBoundary } from '@amadda/external-temporal';
import { FriendFrameHeader } from '@U/components/friend-frame/friend-frame-layout/friend-frame-header';
import { FriendRouter } from '@U/components/friend-frame/friend-router';
import { FriendFrameBody } from '@U/components/friend-frame/friend-frame-layout/friend-frame-body';

export default function FriendPage() {
  return (
    <ErrorBoundary>
      <FriendFrameLayout />
      <FriendRouter />
    </ErrorBoundary>
  );
}
