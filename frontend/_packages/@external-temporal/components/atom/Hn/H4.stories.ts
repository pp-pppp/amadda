import type { Meta, StoryObj } from '@storybook/react';

import { H4 } from './H4';

const meta = {
  title: 'atom/Headings/H4',
  component: H4,
  parameters: {
    componentSubtitle: 'H4 요소입니다.',
    docs: {
      description: {
        component: '커스텀한 h4 태그입니다.',
      },
    },
  },
  tags: ['autodocs'],
  argTypes: {
    children: {
      description: '자식 노드를 받습니다. 텍스트 노드 사용을 권고합니다.',
    },
  },
} satisfies Meta<typeof H4>;

export default meta;
type Story = StoryObj<typeof meta>;

export const Primary: Story = {
  args: {
    children: '안녕 나는 네 번째 제목이야',
  },
};
