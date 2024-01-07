import React from 'react';
import { BtnRound, Flex, H3, Spacing } from '@amadda/external-temporal';
import type { Dispatch, ReactNode, SetStateAction } from 'react';
import { createContext, useState } from 'react';
import { FRAME } from './FriendFrame.css';
import FriendsConstants from '../../../constants/FRIENDS';
import { FriendGroupData } from '../FriendGroupData/FriendGroupData';
import { FriendSearch } from '../FriendSearch/FriendSearch';

export interface FriendsProps {
  children: ReactNode;
}

export const MODE_CONTEXT = createContext<
  [number | 'ADD_GROUP' | 'SEARCH' | 'NOT_EDITING', Dispatch<SetStateAction<number | 'ADD_GROUP' | 'SEARCH' | 'NOT_EDITING'>>]
>(['NOT_EDITING', () => {}]); //어떤 동작을 하는 중인지. groupSeq 번 그룹 편집 중, 그룹 추가 중, 검색 중, 아무 그룹도 편집중이지 않음

export function FriendFrame({ children }: FriendsProps) {
  const [MODE, SET_MODE] = useState<number | 'ADD_GROUP' | 'SEARCH' | 'NOT_EDITING'>('NOT_EDITING');
  return (
    <MODE_CONTEXT.Provider value={[MODE, SET_MODE]}>
      <div className={FRAME}>
        <Flex flexDirection="column" justifyContents="start" alignItems="start">
          <Flex width="fill" justifyContents="spaceBetween">
            <H3>친구</H3>
            <Flex justifyContents="start">
              {MODE === 'ADD_GROUP' ? (
                <BtnRound
                  type="button"
                  variant="white"
                  disabled={false}
                  size="S"
                  onClick={() => SET_MODE('NOT_EDITING')} //그룹추가 조건부 렌더링
                >
                  {FriendsConstants.BTN.CANCEL}
                </BtnRound>
              ) : (
                <BtnRound
                  type="button"
                  variant="white"
                  disabled={false}
                  size="S"
                  onClick={() => SET_MODE('ADD_GROUP')} //그룹추가 조건부 렌더링
                >
                  {FriendsConstants.BTN.ADD_GROUP}
                </BtnRound>
              )}

              <Spacing dir="h" size="0.25" />

              <BtnRound
                type="button"
                variant="black"
                disabled={false}
                size="S"
                onClick={() => SET_MODE('SEARCH')} //친구찾기 조건부 렌더링
              >
                {FriendsConstants.BTN.FIND_FRIEND}
              </BtnRound>
            </Flex>
          </Flex>
          <Spacing dir="v" size="0.5" />
        </Flex>
        <>{MODE === 'NOT_EDITING' ? children : MODE === 'SEARCH' ? <FriendSearch /> : <FriendGroupData />}</>
      </div>
    </MODE_CONTEXT.Provider>
  );
}
