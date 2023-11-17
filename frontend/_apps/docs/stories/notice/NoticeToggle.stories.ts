import type { Meta, StoryObj } from '@storybook/react';

import { NoticeToggle } from '../../../notice/components/NoticeConfig/NoticeToggle';

const meta = {
  title: 'Notice/NoticeToggle',
  component: NoticeToggle,
  parameters: {
    layout: 'centered',
    componentSubtitle: '알림 설정 컴포넌트입니다.',
  },
  tags: ['autodocs'],
  argTypes: {
    alarmType: {
      description: '알림 타입입니다.',
    },
    content: {
      description: '알림 내용입니다.',
    },
  },
} satisfies Meta<typeof NoticeToggle>;

export default meta;
type Story = StoryObj<typeof meta>;

export const Primary: Story = {
  args: {
    alarmType: 'FRIEND_REQUEST',
    content: '민병기님이 친구를 신청했어요.',
    isRead: true,
    alarmSeq: 3,
    isEnabled: true,
  },
};

export const Secondary: Story = {
  args: {
    alarmType: 'SCHEDULE_ASSIGNED',
    content: '민들레떡볶이 모임에 초대되었어요.',
    isRead: true,
    alarmSeq: 2,
    isEnabled: true,
  },
};
