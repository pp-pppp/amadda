import type { Meta, StoryObj } from '@storybook/react';

import { Notice } from '../../../notice/components/Notice/Notice';

const meta = {
  title: 'Notice/Notice',
  component: Notice,
  parameters: {
    layout: 'centered',
    componentSubtitle: '알림 컴포넌트입니다.',
  },
  tags: ['autodocs'],
  argTypes: {
    alarmType: {
      description: 'ss',
    },
    content: {
      description: 'yy',
    },
  },
} satisfies Meta<typeof Notice>;

export default meta;
type Story = StoryObj<typeof meta>;

export const Primary: Story = {
  args: {
    alarmType: 'FRIEND_REQUEST',
    content: '민병기님이 친구 신청을 했어요.',
    isRead: false,
    alarmSeq: 3,
  },
};

export const Secondary: Story = {
  args: {
    alarmType: 'SCHEDULE_ASSIGNED',
    content: '두희가 칼 들고 협박함에 초대됐어요.',
    isRead: true,
    alarmSeq: 2,
  },
};
