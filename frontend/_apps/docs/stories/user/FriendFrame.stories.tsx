import React from 'react';
import type { Meta, StoryObj } from '@storybook/react';

import { FriendFrame } from '../../../user/components/Friend/FriendFrame/FriendFrame';
import { FriendGroups } from '../../../user/components/Friend/FriendGroups/FriendGroups';
import { Friend } from '../../../user/components/Friend/FriendList/FriendList';

const meta = {
  title: 'User/Friend/FriendFrame',
  component: FriendFrame,
  parameters: {
    layout: 'centered',
    componentSubtitle: '친구 리스트의 친구 한 명에 해당하는 컴포넌트입니다.',
  },
  tags: ['autodocs'],
  argTypes: {
    children: {
      description:
        '프레임 안쪽에서 사용되는 Group 컴포넌트입니다. 그룹이 전혀 없더라도 "전체 유저"에 해당하는 그룹이 존재합니다. 안쪽에는 FriendGroups를 비롯해 친구 컴포넌드가 반드시 포함되어야 합니다.',
    },
  },
} satisfies Meta<typeof FriendFrame>;

export default meta;
type Story = StoryObj<typeof meta>;

export const Primary: Story = {
  args: {
    children: (
      <>
        {[
          {
            groupSeq: 3,
            groupName: 'wow',
            groupMember: [
              { userSeq: 1, userName: '김민정', userId: 'zz', imageUrl: '' },
              { userSeq: 2, userName: '정민영', userId: 'zzzz', imageUrl: '' },
              { userSeq: 3, userName: '박동건', userId: 'zzzzz', imageUrl: '' },
              {
                userSeq: 3,
                userName: '권소희',
                userId: 'zzzzzz',
                imageUrl: '',
              },
            ],
          },
        ].map(g => {
          if (g.groupSeq !== null) {
            return (
              <FriendGroups
                key={g.groupSeq}
                groupSeq={g.groupSeq}
                groupName={'전체'}
              >
                {g.groupMember.map(f => (
                  <Friend key={f.userId} {...f} />
                ))}
              </FriendGroups>
            );
          }
        })}
      </>
    ),
  },
};
