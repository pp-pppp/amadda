import React from 'react';
import { BtnRound, Flex, Input, List, Profile, Spacing, Span } from '@amadda/external-temporal';
import { FRIEND_PROFILE } from './Friend.css';
import FRIENDS from '@U/constants/FRIENDS';
import { User } from '@amadda/global-types';

export interface FriendProps {
  groupMember: User;
  selected?: boolean;
  onSelect?: ((f: User) => void) | null;
  onRequest?: ((f: User) => void) | null;
  onQuit?: ((f: User) => void) | null;
}
export function Friend({ groupMember, selected, onSelect = null, onRequest = null, onQuit = null }: FriendProps) {
  return (
    <List.Li display="block">
      <div className={FRIEND_PROFILE}>
        <Flex width="fill" justifyContents="spaceBetween">
          <Flex justifyContents="start">
            <Profile size="small" src={groupMember.imageUrl} alt={groupMember.userName} />
            <Spacing dir="h" size="0.5" />
            <Span>{groupMember.userName}</Span>
          </Flex>
          {typeof onSelect === 'function' && (
            <Input
              type="checkbox"
              name={groupMember.userId}
              disabled={false}
              value={String(groupMember.userSeq)}
              id={groupMember.userId}
              checked={selected}
              onChange={() => onSelect(groupMember)}
            />
          )}
          {typeof onRequest === 'function' && (
            <BtnRound type="button" variant="key" size="S" onClick={() => onRequest(groupMember)} disabled={false}>
              {FRIENDS.BTN.REQUEST_FRIEND}
            </BtnRound>
          )}
          {typeof onQuit === 'function' && (
            <BtnRound type="button" variant="white" size="S" onClick={() => onQuit(groupMember)} disabled={false}>
              {FRIENDS.BTN.QUIT_FRIEND}
            </BtnRound>
          )}
        </Flex>
      </div>
    </List.Li>
  );
}
