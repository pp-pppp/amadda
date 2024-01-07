import type { Meta, StoryObj } from '@storybook/react';
import { MobileMonthlyPlate } from '../../../schedule/components/MobileMonthlyPlate/MobileMonthlyPlate';

const meta = {
  title: 'Schedule/MobileMonthlyPlate',
  component: MobileMonthlyPlate,
  parameters: {
    layout: 'centered',
    componentSubtitle: '모바일 월간 뷰에 보여질 하루 단위 컴포넌트입니다.',
  },
  tags: ['autodocs'],
  argTypes: {
    dateType: {
      description: '평일 혹은 주말을 선택합니다.',
      options: ['weekday', 'weekend'],
    },
    isScheduled: {
      description: '해당 날짜의 일정 유무를 나타냅니다.',
    },
    isSelected: {
      description: '현재 유저가 선택한 날짜인지를 표시합니다.',
    },
    children: {
      description: '날짜입니다.',
    },
  },
} satisfies Meta<typeof MobileMonthlyPlate>;

export default meta;
type Story = StoryObj<typeof meta>;

export const Primary: Story = {
  args: {
    dateType: 'weekday',
    isScheduled: true,
    isSelected: true,
    children: '3',
  },
};
