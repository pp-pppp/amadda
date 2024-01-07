import { H3, H6 } from '@amadda/external-temporal';
import React, { useContext, useState } from 'react';
import { FriendFrame, MODE_CONTEXT } from '../FriendFrame/FriendFrame';
import { useFriend } from '@U/hooks/useFriend';
import { FriendGroups } from '../FriendGroups/FriendGroups';
import FRIENDS from '@U/constants/FRIENDS';
import { Friend } from '../Friend/Friend';
export interface FriendGroupDataProps {
  groupSeq?: number;
  groupName?: string;
  groupMember?: Array<number>;
}
export function FriendGroupData({ groupName, groupSeq, groupMember }: FriendGroupDataProps) {
  const [newGroupName, setNewGroupName] = useState<string>('');
  const [newFriends, setNewFriends] = useState<Array<number>>([]);
  const { data, isLoading, error } = useFriend();

  const [MODE, SET_MODE] = useContext(MODE_CONTEXT);
  const handleNewFriends = f => {
    if (newFriends.includes(f.userSeq)) {
      const filtered = newFriends.filter(i => i !== f.userSeq);
      setNewFriends(filtered);
    } else {
      setNewFriends([...newFriends, f.userSeq]);
    }
  };
  if (MODE === 'ADD_GROUP') {
    //새 친구 넣기
    return (
      <div>
        {data &&
          data.map(g => {
            if (g.groupSeq === null) {
              //전체 그룹만 리턴해야 합니다
              return (
                <FriendGroups
                  key={g.groupSeq}
                  groupSeq={g.groupSeq}
                  groupName={FRIENDS.GROUPS.ALL_FRIENDS}
                  newGroupName={newGroupName}
                  setNewGroupName={setNewGroupName}
                  newFriends={newFriends}
                >
                  {g.groupMember.map(f => (
                    <Friend {...f} key={f.userId} selected={newFriends.includes(f.userSeq)} onSelect={() => handleNewFriends(f)} />
                  ))}
                </FriendGroups>
              );
            }
          })}
      </div>
    );
  }
  if (typeof MODE === 'number') {
    //기존 친구 편집
    return (
      <div>
        {data &&
          data.map(g => {
            if (g.groupSeq !== null) {
              return (
                <FriendGroups key={g.groupSeq} groupSeq={g.groupSeq} groupName={FRIENDS.GROUPS.ALL_FRIENDS} setNewGroupName={setNewGroupName}>
                  {g.groupMember.map(f => (
                    <Friend {...f} key={f.userId} selected={newFriends.includes(f.userSeq)} onSelect={() => handleNewFriends(f)} />
                  ))}
                </FriendGroups>
              );
            }
          })}
      </div>
    );
  } else return <></>;
}
