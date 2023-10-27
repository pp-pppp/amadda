import type { Meta, StoryObj } from '@storybook/react';

import Tooltip from './Tooltip';

// More on how to set up stories at: https://storybook.js.org/docs/react/writing-stories/introduction#default-export
const meta = {
  title: 'molecule/Tooltip',
  component: Tooltip,
  parameters: {
    // Optional parameter to center the component in the Canvas. More info: https://storybook.js.org/docs/react/configure/story-layout
    layout: 'centered',
    componentSubtitle: '한 줄 요약',
    docs: {
      description: {
        component: '컴파운드 컴포넌트인 경우 여기서 설명합니다.',
      },
    },
  },
  // This component will have an automatically generated Autodocs entry: https://storybook.js.org/docs/react/writing-docs/autodocs
  tags: ['autodocs'],
  // More on argTypes: https://storybook.js.org/docs/react/api/argtypes
  argTypes: {
    // backgroundColor: { control: 'color' },
    children: {
      description: '자식 노드를 받습니다. 텍스트 노드 사용을 권고합니다.',
      table: {
        category: 'Tabs', // 추가
      },
    },
  },
} satisfies Meta<typeof Tooltip>;

export default meta;
type Story = StoryObj<typeof meta>;

// More on writing stories with args: https://storybook.js.org/docs/react/writing-stories/args
export const Primary: Story = {
  args: {
    // primary: true,
    children:
      '안녕 나는 툴팁이야. 다른 컴포넌트에 마우스 호버되었을 때 나타났다가 사라질 거야!',
  },
};
