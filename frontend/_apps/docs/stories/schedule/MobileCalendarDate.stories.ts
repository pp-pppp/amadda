import type { Meta, StoryObj } from '@storybook/react';
import { MobileCalendarDate } from '../../../schedule/components/MobileCalendar/MobileCalendarDate/MobileCalendarDate';

const meta = {
  title: 'Schedule/MobileCalendarDate',
  component: MobileCalendarDate,
  parameters: {
    layout: 'centered',
    componentSubtitle: '월간 뷰의 날짜 컴포넌트입니다.',
  },
  tags: ['autodocs'],
  argTypes: {},
} satisfies Meta<typeof MobileCalendarDate>;

export default meta;
type Story = StoryObj<typeof meta>;

export const Primary: Story = {
  args: {},
};
