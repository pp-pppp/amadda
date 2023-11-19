import type { Meta, StoryObj } from '@storybook/react';
import { Category } from '../../../schedule/components/ScheduleEdit/Category/Category';

const meta = {
  title: 'Schedule/Category',
  component: Category,
  parameters: {
    layout: 'centered',
    componentSubtitle: '모바일 일정 생성 페이지입니다.',
  },
  tags: ['autodocs'],
  argTypes: {},
} satisfies Meta<typeof Category>;

export default meta;
type Story = StoryObj<typeof meta>;

export const Primary: Story = {
  args: { color: 'GRAY', categoryName: 'wow' },
};
