import type { Meta, StoryObj } from '@storybook/react';

import { Friend } from '../../../user/components/Friend/FriendList/FriendList';

const meta = {
  title: 'User/Friend/Friend',
  component: Friend,
  parameters: {
    layout: 'centered',
    componentSubtitle: '친구 리스트의 친구 한 명에 해당하는 컴포넌트입니다.',
  },
  tags: ['autodocs'],
  argTypes: {
    userName: {
      description: '닉네임에 해당합니다. 가입 시에 입력합니다.',
    },
    userId: {
      description:
        '유저 아이디에 해당합니다. 가입 시에 입력하는 고유한 아이디입니다.',
    },
    imageUrl: {
      description:
        '프로필 사진입니다. 기본적으로 카카오톡의 프로필 사진을 가져옵니다.',
    },
  },
} satisfies Meta<typeof Friend>;

export default meta;
type Story = StoryObj<typeof meta>;

export const Primary: Story = {
  args: {
    userSeq: 3,
    userName: '민병기',
    userId: 'bminlovecoffee',
    imageUrl:
      'https://amadda-bucket.s3.ap-northeast-2.amazonaws.com/1111_2023-11-10-15-04-51.jpg',
  },
};
