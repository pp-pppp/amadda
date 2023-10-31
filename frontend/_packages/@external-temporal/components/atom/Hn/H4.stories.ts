import type { Meta, StoryObj } from '@storybook/react';

import { H4 } from './H4';

const meta = {
  title: 'atom/Headings/H4',
  component: H4,
  parameters: {
    // layout: 'centered',
  },
  tags: ['autodocs'],
  argTypes: {},
} satisfies Meta<typeof H4>;

export default meta;
type Story = StoryObj<typeof meta>;

export const Primary: Story = {
  args: {
    // primary: true,
    children: '안녕 나는 네 번째 제목이야',
  },
};
