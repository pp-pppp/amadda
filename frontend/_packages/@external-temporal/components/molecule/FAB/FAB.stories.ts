import type { Meta, StoryObj } from '@storybook/react';

import { FAB } from './FAB';

const meta = {
  title: 'molecule/FAB',
  component: FAB,
  parameters: {
    layout: 'centered',
  },
  tags: ['autodocs'],
  argTypes: {},
} satisfies Meta<typeof FAB>;

export default meta;
type Story = StoryObj<typeof meta>;

export const Primary: Story = {
  args: {},
};
