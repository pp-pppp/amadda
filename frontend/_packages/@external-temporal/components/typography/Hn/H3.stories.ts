import type { Meta, StoryObj } from '@storybook/react';

import { H3 } from './H3';

const meta = {
  title: 'External-temporal/Typography/Headings/H3',
  component: H3,
  parameters: {
    componentSubtitle: 'H3 요소입니다.',
    docs: {
      description: {
        component: '커스텀한 h3 태그입니다.',
      },
    },
  },
  tags: ['autodocs'],
  argTypes: {
    children: {
      description: '자식 노드를 받습니다. 텍스트 노드 사용을 권고합니다.',
    },
  },
} satisfies Meta<typeof H3>;

export default meta;
type Story = StoryObj<typeof meta>;

export const Primary: Story = {
  args: {
    children: '안녕 나는 세 번째 제목이야',
  },
};
