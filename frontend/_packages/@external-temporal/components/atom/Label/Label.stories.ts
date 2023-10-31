/* eslint-disable @typescript-eslint/no-unused-vars */
import type { Meta, StoryObj } from '@storybook/react';

import { Label } from './Label';

const meta = {
  title: 'atom/Label',
  component: Label,
  parameters: {
    layout: 'centered',
    componentSubtitle: 'input label',
    docs: {
      description: {
        component: '바인딩 될 input 태그의 상단에 보여질 label입니다.',
      },
    },
  },
  tags: ['autodocs'],
  argTypes: {
    htmlFor: {
      description: '바인딩 될 input 태그의 id입니다.',
    },
    children: {
      description: '자식 노드를 받습니다. 텍스트 노드 사용을 권고합니다.',
    },
  },
} satisfies Meta<typeof Label>;

export default meta;
type Story = StoryObj<typeof meta>;

export const Text: Story = {
  args: {
    htmlFor: 'sample-text',
    children: '이메일을 입력해주세요.',
  },
};
