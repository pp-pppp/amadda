import React from 'react';
import { Input, Spacing } from '@amadda/external-temporal';
import FriendsConstants from '@/constants/friend/friend';
import { useState } from 'react';
import { useSearchFriend, useSearchUser } from '@/hooks/friend/use-friend';
import { FriendGroups } from '../friend-groups/friend-groups';
import { Friend } from '../friend-component/friend';
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
