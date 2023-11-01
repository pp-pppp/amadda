import type { Meta, StoryObj } from '@storybook/react';

import { H5 } from './H5';

const meta = {
  title: 'atom/Headings/H5',
  component: H5,
  parameters: {
    componentSubtitle: 'H5 요소입니다.',
    docs: {
      description: {
        component: '커스텀한 h5 태그입니다.',
      },
    },
  },
  tags: ['autodocs'],
  argTypes: {
    children: {
      description: '자식 노드를 받습니다. 텍스트 노드 사용을 권고합니다.',
    },
  },
} satisfies Meta<typeof H5>;

export default meta;
type Story = StoryObj<typeof meta>;

export const Primary: Story = {
  args: {
    children: '안녕 나는 다섯 번째 제목이야',
  },
};
