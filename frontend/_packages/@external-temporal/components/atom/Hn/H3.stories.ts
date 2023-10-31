import type { Meta, StoryObj } from '@storybook/react';

import { H3 } from './H3';

const meta = {
  title: 'atom/Headings/H3',
  component: H3,
  parameters: {
    // layout: 'centered',
  },
  tags: ['autodocs'],
  argTypes: {},
} satisfies Meta<typeof H3>;

export default meta;
type Story = StoryObj<typeof meta>;

export const Primary: Story = {
  args: {
    // primary: true,
    children: '안녕 나는 세 번째 제목이야',
  },
};
