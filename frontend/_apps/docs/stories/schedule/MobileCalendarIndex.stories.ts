import type { Meta, StoryObj } from '@storybook/react';
import { MobileCalendarIndex } from '../../../schedule/components/MobileCalendar/MobileCalendarIndex/MobileCalendarIndex';

const meta = {
  title: 'Schedule/MobileCalendarIndex',
  component: MobileCalendarIndex,
  parameters: {
    layout: 'centered',
    componentSubtitle: '모바일 월간 뷰의 요일 컴포넌트입니다.',
  },
  tags: ['autodocs'],
  argTypes: {},
} satisfies Meta<typeof MobileCalendarIndex>;

export default meta;
type Story = StoryObj<typeof meta>;

export const Primary: Story = {
  args: {},
};
