import type { Meta, StoryObj } from '@storybook/react';

import { Icon } from './Icon';

const meta = {
  title: 'atom/Icon',
  component: Icon,
  parameters: {
    // layout: 'centered',
  },
  tags: ['autodocs'],
  argTypes: {},
} satisfies Meta<typeof Icon>;

export default meta;
type Story = StoryObj<typeof meta>;

export const Primary: Story = {
  args: {
    type: 'cal',
    color: 'black',
  },
};
export const Secondary: Story = {
  args: {
    type: 'friends',
    color: 'key',
  },
};
