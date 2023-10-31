import type { Meta, StoryObj } from '@storybook/react';

import { H1 } from './H1';

const meta = {
  title: 'atom/Headings/H1',
  component: H1,
  parameters: {
    // layout: 'centered',
  },
  tags: ['autodocs'],
  argTypes: {},
} satisfies Meta<typeof H1>;

export default meta;
type Story = StoryObj<typeof meta>;

export const Primary: Story = {
  args: {
    // primary: true,
    children: '안녕 나는 제목이야',
  },
};
