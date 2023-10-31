import type { Meta, StoryObj } from '@storybook/react';

import { Hr } from './Hr';

const meta = {
  title: 'atom/Hr',
  component: Hr,
  parameters: {
    // layout: 'centered',
  },
  tags: ['autodocs'],
  argTypes: {},
} satisfies Meta<typeof Hr>;

export default meta;
type Story = StoryObj<typeof meta>;

export const Primary: Story = {
  args: {},
};
