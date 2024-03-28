import React from 'react';
import type { ReactNode } from 'react';
import { FRAME } from './friend-frame.css';
import { FriendFrameHeader } from './friend-frame-header';
import { FriendRouter } from '../friend-router';
import { FriendFrameBody } from './friend-frame-body';

export interface FriendFrameProps {
  children: ReactNode;
}
export function FriendFrameLayout() {
  return (
    <div className={FRAME}>
      <FriendFrameHeader />
      <FriendFrameBody>
        <FriendRouter />
      </FriendFrameBody>
    </div>
  );
}
