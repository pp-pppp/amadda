import React, { useContext } from 'react';
import { BtnRound, Flex, Input, List, Profile, Spacing, Span } from '@amadda/external-temporal';
import { FRIEND_PROFILE } from './Friend.css';
import FRIENDS from '@U/constants/FRIENDS';
import { useFriendRouter } from '@U/store/friendRouter/useFriendRouter';
import { ROUTES } from '@U/store/friendRouter/pageSlice';

export interface FriendProps {
  groupMember: {
    userSeq: number;
    userName: string;
    userId: string;
    imageUrl: string;
  };
  status: (typeof ROUTES)[number];
  selected?: boolean;
  onSelect?: (f: number) => void;
  onRequest?: (f: number) => void;
  onQuit?: (f: number) => void;
}
export function Friend({ groupMember, status, selected, onSelect = () => {}, onRequest = () => {}, onQuit = () => {} }: FriendProps) {
  return (
    <List.Li display="block">
      <div className={FRIEND_PROFILE}>
        <Flex width="fill" justifyContents="spaceBetween">
          <Flex justifyContents="start">
            <Profile size="small" src={groupMember.imageUrl} alt={groupMember.userName} />
            <Spacing dir="h" size="0.5" />
            <Span>{groupMember.userName}</Span>
          </Flex>
          {status === 'EDIT' && (
            <Input
              type="checkbox"
              name={groupMember.userId}
              disabled={false}
              value={String(groupMember.userSeq)}
              id={groupMember.userId}
              checked={selected}
              onChange={() => onSelect(groupMember.userSeq)}
            />
          )}
          {status === 'SEARCH' && (
            <BtnRound type="button" variant="key" size="S" onClick={() => onRequest(groupMember.userSeq)} disabled={false}>
              {FRIENDS.BTN.REQUEST_FRIEND}
            </BtnRound>
          )}
          {status === 'READ' && (
            <BtnRound type="button" variant="white" size="S" onClick={() => onQuit(groupMember.userSeq)} disabled={false}>
              {FRIENDS.BTN.QUIT_FRIEND}
            </BtnRound>
          )}
        </Flex>
      </div>
    </List.Li>
  );
}
