import type { Meta, StoryObj } from '@storybook/react';

import { Btn } from './Btn';
import React from 'react';

const meta = {
  title: 'atom/Buttons/Btn',
  component: Btn,
  parameters: {
    layout: 'centered',
  },
  tags: ['autodocs'],
  argTypes: {},
} satisfies Meta<typeof Btn>;

export default meta;
type Story = StoryObj<typeof meta>;

export const Primary: Story = {
  args: {
    type: 'button',
    variant: 'key',
    disabled: false,
    children: '안녕 나는 네모 버튼이야',
    onClick: (e: React.MouseEvent) => {
      console.log(e);
    },
  },
};

export const Secondary: Story = {
  args: {
    type: 'button',
    variant: 'white',
    disabled: false,
    children: '네모네모 스펀지송',
  },
};

export const Tertiary: Story = {
  args: {
    type: 'button',
    variant: 'black',
    disabled: false,
    children: '네모바지 스펀지밥',
  },
};
