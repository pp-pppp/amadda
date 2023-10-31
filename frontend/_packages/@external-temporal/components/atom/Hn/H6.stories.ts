import type { Meta, StoryObj } from '@storybook/react';

import { H6 } from './H6';

const meta = {
  title: 'atom/Headings/H6',
  component: H6,
  parameters: {
    // layout: 'centered',
  },
  tags: ['autodocs'],
  argTypes: {},
} satisfies Meta<typeof H6>;

export default meta;
type Story = StoryObj<typeof meta>;

export const Primary: Story = {
  args: {
    // primary: true,
    children: '안녕 나는 여섯 번째 제목이야',
  },
};
