import type { Meta, StoryObj } from '@storybook/react';

import { Tooltip } from './Tooltip';

const meta = {
  title: 'External-temporal/Views/Tooltip',
  component: Tooltip,
  parameters: {
    layout: 'centered',
    componentSubtitle: '컴포넌트에 대해 설명하는 Tooltip입니다.',
    docs: {
      description: {
        component: '기능에 대한 간단한 설명을 제공하는 컴포넌트입니다. 주로 다른 컴포넌트를 호버했을 때 보입니다.',
      },
    },
  },
  tags: ['autodocs'],
  argTypes: {
    children: {
      description: '자식 노드를 받습니다. 텍스트 노드 사용을 권고합니다.',
    },
  },
} satisfies Meta<typeof Tooltip>;

export default meta;
type Story = StoryObj<typeof meta>;

export const Primary: Story = {
  args: {
    // primary: true,
    children: '안녕 나는 툴팁이야. 다른 컴포넌트에 마우스 호버되었을 때 나타났다가 사라질 거야!',
  },
};
