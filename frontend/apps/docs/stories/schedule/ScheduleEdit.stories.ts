import type { Meta, StoryObj } from '@storybook/react';
import { ScheduleEdit } from '../../../schedule/components/ScheduleEdit/ScheduleEditForm/ScheduleEditForm';

const meta = {
  title: 'Schedule/ScheduleEdit',
  component: ScheduleEdit,
  parameters: {
    layout: 'centered',
    componentSubtitle: '모바일 일정 생성 페이지입니다.',
  },
  tags: ['autodocs'],
  argTypes: {},
} satisfies Meta<typeof ScheduleEdit>;

export default meta;
type Story = StoryObj<typeof meta>;

export const Primary: Story = {
  args: {},
};
