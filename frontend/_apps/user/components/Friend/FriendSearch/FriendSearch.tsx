import React from 'react';
import { Input, Spacing } from 'external-temporal';
import FriendsConstants from '../../../constants/FRIENDS';
import { useState } from 'react';
import { FriendFrame } from '../FriendFrame/FriendFrame';
import { useSearchFriend, useSearchUser } from '@U/hooks/useFriend';
import { FriendGroups } from '../FriendGroups/FriendGroups';
import FRIENDS from '../../../constants/FRIENDS';
import { Friend } from '../FriendList/FriendList';
import { http } from '@U/utils/http';

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
      {/* 친구 리스트 여기 다 들어오삼 검색결과로 ㅇㅇ 그룹 안ㄴ에 들어오면됨 */}
      <Spacing dir="v" size="0.5" />
      {newFriendData.data && !newFriendData.data.isFriend && (
        <Friend
          {...newFriendData.data}
          key={newFriendData.data.userId}
          status="request"
          onRequest={async () =>
            http.post(`${process.env.NEXT_PUBLIC_USER}/friend/request`, {
              targetSeq: newFriendData.data?.userSeq,
            })
          }
        />
      )}
      <Spacing dir="v" size="0.5" />
      {friendData.data &&
        friendData.data.map(g => (
          <FriendGroups
            key={g.groupSeq}
            groupSeq={g.groupSeq}
            groupName={g.groupName}
          >
            {g.groupMember.map(f => (
              <Friend {...f} key={f.userId} status="request" />
            ))}
          </FriendGroups>
        ))}
    </>
  );
}
