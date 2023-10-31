import type { Meta, StoryObj } from '@storybook/react';

import { BtnRound } from './BtnRound';

const meta = {
  title: 'atom/Buttons/BtnRound',
  component: BtnRound,
  parameters: {
    layout: 'centered',
  },
  tags: ['autodocs'],
  argTypes: {},
} satisfies Meta<typeof BtnRound>;

export default meta;
type Story = StoryObj<typeof meta>;

export const Primary: Story = {
  args: {
    // primary: true,
    type: 'button',
    variant: 'key',
    disabled: false,
    size: 'M',
    children: '안녕 나는 둥근 버튼이야',
  },
};
