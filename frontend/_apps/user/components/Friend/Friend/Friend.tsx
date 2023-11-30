import React, { useContext } from 'react';
import { BtnRound, Flex, Input, List, Profile, Spacing, Span } from 'external-temporal';
import { FRIEND_PROFILE } from '../FriendGroups/Friends.css';
import FRIENDS from '@U/constants/FRIENDS';
import { MODE_CONTEXT } from '../FriendFrame/FriendFrame';

export interface FriendProps {
  userSeq: number;
  userName: string;
  userId: string;
  imageUrl: string;
  status?: 'edit' | 'request' | 'normal' | 'quit';
  selected?: boolean;
  onSelect?: (f: number) => void;
  onRequest?: (f: number) => void;
  onQuit?: (f: number) => void;
}
export function Friend({
  userSeq,
  userName,
  userId,
  imageUrl,
  status = 'normal',
  selected,
  onSelect = () => {},
  onRequest = () => {},
  onQuit = () => {},
}: FriendProps) {
  const [EDITING_GROUP] = useContext(MODE_CONTEXT);
  return (
    <List.Li display="block">
      <div className={FRIEND_PROFILE}>
        <Flex width="fill" justifyContents="spaceBetween">
          <Flex justifyContents="start">
            <Profile size="small" src={imageUrl} alt={userName} />
            <Spacing dir="h" size="0.5" />
            <Span>{userName}</Span>
          </Flex>
          {(status === 'edit' || EDITING_GROUP !== 'NOT_EDITING') && (
            <>
              <Input type="checkbox" name={userId} disabled={false} value={String(userSeq)} id={userId} checked={selected} onChange={() => onSelect(userSeq)} />
            </>
          )}
          {status === 'request' && (
            <>
              <BtnRound type="button" variant="key" size="S" onClick={() => onRequest(userSeq)} disabled={false}>
                {FRIENDS.BTN.REQUEST_FRIEND}
              </BtnRound>
            </>
          )}
          {status === 'quit' && (
            <>
              <>
                <BtnRound type="button" variant="white" size="S" onClick={() => onQuit(userSeq)} disabled={false}>
                  {FRIENDS.BTN.QUIT_FRIEND}
                </BtnRound>
              </>
            </>
          )}
        </Flex>
      </div>
    </List.Li>
  );
}
