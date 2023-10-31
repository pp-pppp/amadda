import type { Meta, StoryObj } from '@storybook/react';

import { Spacing } from './Spacing';

const meta = {
  title: 'atom/Spacing',
  component: Spacing,
  parameters: {
    // layout: 'centered',
  },
  tags: ['autodocs'],
  argTypes: {},
} satisfies Meta<typeof Spacing>;

export default meta;
type Story = StoryObj<typeof meta>;

export const Primary: Story = {
  args: {
    // primary: true,
    dir: 'v',
    size: '3',
  },
};
export const Secondary: Story = {
  args: {
    // primary: true,
    dir: 'h',
    size: '1',
  },
};
