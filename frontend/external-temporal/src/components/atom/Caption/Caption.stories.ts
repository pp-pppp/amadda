import type { Meta, StoryObj } from '@storybook/react';

import Caption from './Caption';

const meta = {
  title: 'atom/Caption',
  component: Caption,
  parameters: {
    layout: 'centered',
  },
  tags: ['autodocs'],
  argTypes: {},
} satisfies Meta<typeof Caption>;

export default meta;
type Story = StoryObj<typeof meta>;

export const Primary: Story = {
  args: {
    children: '실명 또는 실명에 가까운 별명을 넣어주세요.',
  },
};
