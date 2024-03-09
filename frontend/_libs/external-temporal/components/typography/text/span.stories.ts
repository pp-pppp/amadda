import type { Meta, StoryObj } from '@storybook/react';

import { Span } from './span';

const meta = {
  title: 'External-temporal/Typography/Texts/Span',
  component: Span,
  parameters: {
    layout: 'centered',
    componentSubtitle: '기본 텍스트 입력 시 사용되는 span 태그입니다.',
  },
  tags: ['autodocs'],
  argTypes: {
    color: {
      description: '글자의 색상을 선택할 수 있습니다.',
    },
    children: {
      description: '자식 노드를 받습니다. 텍스트 노드 사용을 권고합니다.',
    },
  },
} satisfies Meta<typeof Span>;

export default meta;
type Story = StoryObj<typeof meta>;

export const Primary: Story = {
  args: {
    children: '안녕 나는 제목이야',
  },
};
