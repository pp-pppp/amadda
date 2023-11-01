import type { Meta, StoryObj } from '@storybook/react';

import { Flex } from './Flex';

const meta = {
  title: 'layout/Flex',
  component: Flex,
  parameters: {
    layout: 'centered',
    componentSubtitle: '여러 컴포넌트들을 정렬할 수 있습니다.',
  },
  tags: ['autodocs'],
  argTypes: {
    flexDirection: {
      description: 'css의 flex-direction 속성을 받습니다.',
    },
    justifyContents: {
      description: 'css의 justify-contents 속성을 받습니다.',
    },
    alignItems: {
      description: 'css의 align-items 속성을 받습니다.',
    },
    flexWrap: {
      description: 'css의 flex-wrap 속성을 받습니다.',
    },
    children: {
      description: '하위 컴포넌트들입니다.',
    },
  },
} satisfies Meta<typeof Flex>;

export default meta;
type Story = StoryObj<typeof meta>;

export const Primary: Story = {
  args: {
    justifyContents: 'center',
    children: '안녕',
  },
};
