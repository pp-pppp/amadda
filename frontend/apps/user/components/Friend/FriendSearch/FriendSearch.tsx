import React from 'react';
import { Input, Spacing } from '@amadda/external-temporal';
import FriendsConstants from '../../../constants/FRIENDS';
import { useState } from 'react';
import { FriendFrame } from '../FriendFrame/FriendFrame';
import { useSearchFriend, useSearchUser } from '@U/hooks/useFriend';
import { FriendGroups } from '../FriendGroups/FriendGroups';
import FRIENDS from '../../../constants/FRIENDS';
import { Friend } from '../Friend/Friend';
import { clientFetch } from '@amadda/fetch';

export interface FriendSearchProps {}
export function FriendSearch({}: FriendSearchProps) {
  const [searchKey, setSearchKey] = useState<string>('');
  const friendData = useSearchFriend(searchKey);
  const newFriendData = useSearchUser(searchKey);
  return (
    <>
      <Input
        type="text"
        id="find_friend"
        name="find_friend"
        disabled={false}
        value={searchKey}
        onChange={e => setSearchKey(e.target.value)}
        placeholder={FriendsConstants.INPUT.SEARCH}
      />
      <Spacing dir="v" size="0.5" />
      {newFriendData.data && !newFriendData.data.isFriend && (
        <Friend
          {...newFriendData.data}
          key={newFriendData.data.userId}
          status="request"
          onRequest={async () =>
            clientFetch.post(`${process.env.NEXT_PUBLIC_USER}/friend/request`, {
              targetSeq: newFriendData.data?.userSeq,
            })
          }
        />
      )}
      <Spacing dir="v" size="0.5" />
      {friendData.data &&
        friendData.data.map(g => (
          <FriendGroups key={g.groupSeq} groupSeq={g.groupSeq} groupName={g.groupName}>
            {g.groupMember.map(f => (
              <Friend {...f} key={f.userId} status="request" />
            ))}
          </FriendGroups>
        ))}
    </>
  );
}
