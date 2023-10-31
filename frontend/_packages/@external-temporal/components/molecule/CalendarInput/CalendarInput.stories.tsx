import React from 'react';
import type { Meta, StoryObj } from '@storybook/react';

import { CalendarInput } from './CalendarInput';

const meta = {
  title: 'molecule/CalendarInput',
  component: CalendarInput,
  parameters: {
    layout: 'centered',
  },
  tags: ['autodocs'],
  argTypes: {},
} satisfies Meta<typeof CalendarInput>;

export default meta;
type Story = StoryObj<typeof meta>;

export const Primary: Story = {
  args: {
    id1: 'hello',
    id2: 'hello2',
    name: 'adfasdf',
    onChange: e => {},
    disabled: false,
    date: '2023.10.31',
    time: '18:00',
    children: (
      <CalendarInput.MiniCalendar
        isShow={true}
        selectedDate="2023.10.31"
      ></CalendarInput.MiniCalendar>
    ),
  },
};
