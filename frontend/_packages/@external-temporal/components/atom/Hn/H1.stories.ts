import type { Meta, StoryObj } from '@storybook/react';

import { H1 } from './H1';

const meta = {
  title: 'atom/Headings/H1',
  component: H1,
  parameters: {
    componentSubtitle: 'H1 요소',
    docs: {
      description: {
        component: '커스텀한 h1 태그입니다.',
      },
    },
  },
  tags: ['autodocs'],
  argTypes: {
    children: {
      description: '자식 노드를 받습니다. 텍스트 노드 사용을 권고합니다.',
    },
  },
} satisfies Meta<typeof H1>;

export default meta;
type Story = StoryObj<typeof meta>;

export const Primary: Story = {
  args: {
    children: '안녕 나는 제목이야',
  },
};
