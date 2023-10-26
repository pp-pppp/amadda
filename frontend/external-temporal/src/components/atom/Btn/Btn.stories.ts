import type { Meta, StoryObj } from '@storybook/react';

import Btn from './Btn';

// More on how to set up stories at: https://storybook.js.org/docs/react/writing-stories/introduction#default-export
const meta = {
  title: 'atom/Buttons/Btn',
  component: Btn,
  parameters: {
    // Optional parameter to center the component in the Canvas. More info: https://storybook.js.org/docs/react/configure/story-layout
    layout: 'centered',
  },
  // This component will have an automatically generated Autodocs entry: https://storybook.js.org/docs/react/writing-docs/autodocs
  tags: ['autodocs'],
  // More on argTypes: https://storybook.js.org/docs/react/api/argtypes
  argTypes: {
    // backgroundColor: { control: 'color' },
  },
} satisfies Meta<typeof Btn>;

export default meta;
type Story = StoryObj<typeof meta>;

// More on writing stories with args: https://storybook.js.org/docs/react/writing-stories/args
export const Primary: Story = {
  args: {
    type: 'button',
    variant: 'key',
    disabled: false,
    children: '안녕 나는 네모 버튼이야',
  },
};

export const Secondary: Story = {
  args: {
    type: 'button',
    variant: 'white',
    disabled: false,
    children: '네모네모 스펀지송',
  },
};

export const Tertiary: Story = {
  args: {
    type: 'button',
    variant: 'black',
    disabled: false,
    children: '네모바지 스펀지밥',
  },
};
