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

export const None: Story = {
  args: {
    type: 'none',
  },
};

export const Desc: Story = {
  args: {
    type: 'desc',
    caption: '실명 또는 실명에 가까운 별명을 넣어주세요.',
  },
};

export const Warn: Story = {
  args: {
    type: 'warn',
    caption: '아이디에는 영소문자와 숫자만 사용해주세요.',
  },
};
