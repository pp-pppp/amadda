import type { Meta, StoryObj } from '@storybook/react';

import { Tooltip } from './Tooltip';

const meta = {
  title: 'molecule/Tooltip',
  component: Tooltip,
  parameters: {
    layout: 'centered',
    componentSubtitle: '한 줄 요약',
    docs: {
      description: {
        component: '컴파운드 컴포넌트인 경우 여기서 설명합니다.',
      },
    },
  },
  tags: ['autodocs'],
  argTypes: {
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

export const Primary: Story = {
  args: {
    // primary: true,
    children:
      '안녕 나는 툴팁이야. 다른 컴포넌트에 마우스 호버되었을 때 나타났다가 사라질 거야!',
  },
};
