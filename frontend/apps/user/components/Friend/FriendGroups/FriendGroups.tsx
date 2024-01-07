import React from 'react';
import type { Dispatch, ReactNode, SetStateAction } from 'react';
import { useState, useContext } from 'react';
import { Flex, Icon, List, Spacing } from '@amadda/external-temporal';
import { FOLD_VARIANT, FRIEND_LIST, GROUP_EDIT, GROUP_LAYOUT, GROUP_NAME, GROUP_NAME_CHANGE, TOGGLE } from './Friends.css';

import { MODE_CONTEXT } from '../FriendFrame/FriendFrame';
import FriendsConstants from '../../../constants/FRIENDS';
import { useRouter } from 'next/router';
import { http } from '@U/utils/http';
import { GroupCreateRequest, GroupUpdateRequest } from '@amadda/global-types';

export interface FriendGroupProps {
  groupSeq: number | null;
  groupName: string;
  setNewGroupName?: (arg: string) => void;
  newGroupName?: string;
  newFriends?: Array<number>;
  children: ReactNode;
}

export function FriendGroups({ groupSeq = null, groupName, children, setNewGroupName, newGroupName, newFriends }: FriendGroupProps) {
  const [EDITING_GROUP] = useContext(MODE_CONTEXT);
  const [fold, setFold] = useState<boolean>(false);

  return (
    <FriendGroups.Group
      groupSeq={groupSeq}
      EDITING_GROUP={EDITING_GROUP}
      groupName={groupName}
      setNewGroupName={setNewGroupName}
      newGroupName={newGroupName}
      newFriends={newFriends}
      fold={fold}
      setFold={setFold}
    >
      {children}
    </FriendGroups.Group>
  );
}

FriendGroups.Group = function ({ groupSeq, EDITING_GROUP, groupName, fold, setFold, setNewGroupName, newGroupName, newFriends, children }) {
  return (
    <div className={GROUP_LAYOUT}>
      <Flex width="fill" flexDirection="column" justifyContents="spaceBetween" alignItems="start">
        <FriendGroups.Title groupName={groupName} newGroupName={newGroupName} fold={fold} onToggle={setFold} setNewGroupName={setNewGroupName} />

        <div className={`${FRIEND_LIST} ${FOLD_VARIANT[fold ? 'fold' : 'unfold']}`}>
          <Flex width="fill" justifyContents="flexEnd">
            <FriendGroups.Edit groupName={groupName} groupSeq={groupSeq} newGroupName={setNewGroupName} newFriends={newFriends} />
          </Flex>
          <List.Ul>{children}</List.Ul>
        </div>
      </Flex>
    </div>
  );
};

FriendGroups.Title = function ({ groupName, newGroupName, setNewGroupName, fold, onToggle }) {
  const [EDITING_GROUP] = useContext(MODE_CONTEXT);
  if (EDITING_GROUP === 'NOT_EDITING')
    return (
      <button className={TOGGLE} onClick={e => onToggle(!fold)}>
        <Flex width="fill" justifyContents="spaceBetween">
          <span className={GROUP_NAME}>{groupName}</span>

          <Icon type={fold ? 'down' : 'up'} color="darkgrey" cursor="pointer" />
        </Flex>
      </button>
    );
  if (EDITING_GROUP === 'ADD_GROUP')
    return (
      <div className={TOGGLE}>
        <Flex width="fill" justifyContents="spaceBetween">
          <input className={GROUP_NAME_CHANGE} placeholder={'그룹 이름을 입력하세요'} value={newGroupName} onChange={e => setNewGroupName(e.target.value)} />
          <Icon type={fold ? 'down' : 'up'} color="darkgrey" cursor="pointer" />
        </Flex>
      </div>
    );
  if (typeof EDITING_GROUP === 'number')
    return (
      <div className={TOGGLE}>
        <Flex width="fill" justifyContents="spaceBetween">
          <input className={GROUP_NAME_CHANGE} placeholder={groupName} onChange={e => setNewGroupName(e.target.value)} />
          <Icon type={fold ? 'down' : 'up'} color="darkgrey" cursor="pointer" />
        </Flex>
      </div>
    );
};

FriendGroups.Edit = function ({ groupName, groupSeq, newGroupName, newFriends }) {
  const router = useRouter();
  const handleDelete = async () => {
    const res = await http
      .delete(`${process.env.NEXT_PUBLIC_USER}/friend/group/${groupSeq}`)
      .then(res => res.data)
      .catch(err => '');
    router.push(`/main`);
  };
  const handleSubmit = async () => {
    if (MODE === 'NOT_EDITING') {
      SET_MODE(groupSeq);
    } else if (MODE === 'ADD_GROUP') {
      const res = await http
        .post<GroupCreateRequest>(`${process.env.NEXT_PUBLIC_USER}/friend/group`, {
          groupName: newGroupName,
          userSeqs: newFriends,
        })
        .then(res => res.data)
        .catch(err => '');
      router.push(`/schedule/${res}`);
      SET_MODE('NOT_EDITING');
    } else if (typeof MODE === 'number') {
      const res = await http
        .put<GroupUpdateRequest>(`${process.env.NEXT_PUBLIC_USER}/friend/group`, {
          groupName: newGroupName.length === 0 ? groupName : newGroupName,
          userSeqs: newFriends,
        })
        .catch(err => '');
      router.push(`/schedule/${res}`);
    }
  };
  const [MODE, SET_MODE] =
    useContext<[number | 'ADD_GROUP' | 'SEARCH' | 'NOT_EDITING', Dispatch<SetStateAction<number | 'ADD_GROUP' | 'SEARCH' | 'NOT_EDITING'>>]>(MODE_CONTEXT);

  if (groupSeq === null) return <></>;

  if (MODE === 'ADD_GROUP')
    return (
      <button type="button" className={GROUP_EDIT.SAVE} disabled={false} onClick={handleSubmit}>
        {FriendsConstants.BTN.ADD_GROUP}
      </button>
    );
  if (typeof MODE === 'number')
    return (
      <Flex justifyContents="flexEnd">
        <button type="button" className={GROUP_EDIT.EDIT} disabled={false} onClick={() => SET_MODE('NOT_EDITING')}>
          {FriendsConstants.BTN.CANCEL}
        </button>
        <Spacing dir="h" size="0.25" />
        <button type="button" className={GROUP_EDIT.SAVE} disabled={false} onClick={handleSubmit}>
          {FriendsConstants.BTN.GROUP_SAVE}
        </button>
      </Flex>
    );
  if (MODE === 'NOT_EDITING')
    return (
      <button type="button" className={GROUP_EDIT.EDIT} disabled={false} onClick={handleSubmit}>
        {FriendsConstants.BTN.GROUP_EDIT}
      </button>
    );

  return (
    //아무 상태도 아닐 때에는 삭제 버튼이 나와야 합니다
    <Flex justifyContents="flexEnd">
      <button type="button" className={GROUP_EDIT.DELETE} disabled={false} onClick={handleDelete}>
        {FriendsConstants.BTN.GROUP_DELETE}
      </button>
      <Spacing dir="h" size="0.25" />
      <button type="button" className={GROUP_EDIT.SAVE} disabled={false} onClick={handleSubmit}>
        {FriendsConstants.BTN.GROUP_SAVE}
      </button>
    </Flex>
  );
};
