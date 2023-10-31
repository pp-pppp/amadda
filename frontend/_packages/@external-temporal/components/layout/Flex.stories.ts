import type { Meta, StoryObj } from '@storybook/react';

import { Flex } from './Flex';

// More on how to set up stories at: https://storybook.js.org/docs/react/writing-stories/introduction#default-export
const meta = {
  title: 'layout/Flex',
  component: Flex,
  parameters: {
    // Optional parameter to center the component in the Canvas. More info: https://storybook.js.org/docs/react/configure/story-layout
    layout: 'centered',
    componentSubtitle: '여러 컴포넌트들을 정렬할 수 있습니다.',
  },
  // This component will have an automatically generated Autodocs entry: https://storybook.js.org/docs/react/writing-docs/autodocs
  tags: ['autodocs'],
  // More on argTypes: https://storybook.js.org/docs/react/api/argtypes
  argTypes: {
    // backgroundColor: { control: 'color' },
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

// More on writing stories with args: https://storybook.js.org/docs/react/writing-stories/args
export const Primary: Story = {
  args: {
    justifyContents: 'center',
    children: '안녕',
  },
};
